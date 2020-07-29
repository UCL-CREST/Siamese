    private void prepareStatement(Method method, String sql) {
        if (sql.startsWith(SPSign)) {
            if (factory.sqlProvider != null) {
                String s = factory.sqlProvider.sql(method, sql.substring(SPSign.length()));
                if (s != null) {
                    sql = s;
                }
            }
        }
        GenericClass[] pts;
        {
            Type[] types = method.getGenericParameterTypes();
            pts = new GenericClass[types.length];
            for (int i = 0; i < types.length; i++) {
                pts[i] = new GenericClass(types[i]);
            }
        }
        if (pts.length > 0 && PreparedStatementProvider.class.isAssignableFrom(pts[0].toClass())) {
            prepareStatementWithProvider(sql);
            return;
        }
        if (sql.startsWith(SPSign)) {
            throw new IException("can not find sql from sql provider: " + sql.substring(SPSign.length()) + " in " + method);
        }
        Stack<Var> varStack = new Stack<Var>();
        Stack<VarContext> varContextStack = new Stack<VarContext>();
        VarContextImpl vci = new VarContextImpl();
        for (int i = 1; i <= pts.length; i++) {
            VarImpl var = new VarImpl();
            var.type = pts[i - 1];
            var.var = "$" + i;
            vci.map.put("arg" + i, var);
        }
        {
            VarImpl var = new VarImpl();
            var.type = new GenericClass(cls);
            var.var = "this";
            vci.map.put("this", var);
            if (pts.length == 0) {
                varStack.push(var);
            }
        }
        varContextStack.push(vci);
        if (pts.length == 1) {
            VarImpl var = new VarImpl();
            var.type = pts[0];
            var.var = "$1";
            varStack.push(var);
        }
        String[] pns = findParamNames(method);
        for (int i = 0; i < pns.length; i++) {
            if (pns[i] == null) continue;
            VarImpl var = new VarImpl();
            var.type = pts[i];
            var.var = "$" + (i + 1);
            vci.map.put(pns[i], var);
        }
        StringBuilder sb = new StringBuilder();
        int end = 0;
        Pattern pat = Pattern.compile("#.+?#|\\?\\d*|\\$.+?\\$");
        Matcher matcher = pat.matcher(sql);
        List<Expression> params = new ArrayList<Expression>();
        List<UserTypeInfo> utis = new ArrayList<UserTypeInfo>();
        int qidx = 1;
        while (matcher.find()) {
            if (end != matcher.start()) {
                sb.append(Utils.strSrc(sql.substring(end, matcher.start())));
            }
            end = matcher.end();
            String s = matcher.group();
            Expression exp = null;
            UserTypeInfo uti = null;
            try {
                if (s.startsWith("#")) {
                    s = s.substring(1, s.length() - 1);
                    int idx = s.lastIndexOf(':');
                    if (idx != -1) {
                        uti = factory.userTypeInfoMap.get(s.substring(idx + 1));
                        if (uti != null) {
                            s = s.substring(0, idx);
                        }
                    }
                    exp = new ExpParser(varStack, varContextStack).parse(s);
                } else if (s.startsWith("$")) {
                    sb.append("\" + ");
                    sb.append(new ExpParser(varStack, varContextStack).parse(s = s.substring(1, s.length() - 1)).getEvalString());
                    sb.append(" + \"");
                } else {
                    int qi;
                    if (s.length() > 1) {
                        qi = Integer.parseInt(s.substring(1));
                    } else {
                        qi = qidx++;
                    }
                    if (qi > pts.length) {
                        throw new IException("there are too few parameters in " + method);
                    }
                    exp = new ExpParser(varStack, varContextStack).parse("arg" + qi);
                }
            } catch (ParseException e) {
                throw new IException("can not parse expression \"" + s + "\" in " + method);
            }
            if (exp != null) {
                if (uti == null) {
                    if (!Utils.isBasicType(exp.getReturnType().toClass())) {
                        for (Map.Entry<String, UserTypeInfo> entry : factory.userTypeInfoMap.entrySet()) {
                            if (entry.getValue().bCls.equals(exp.getReturnType().toClass())) {
                                uti = entry.getValue();
                                break;
                            }
                        }
                        if (uti == null) {
                            throw new IException("parameter \"" + s + "\" is not basic type in " + method);
                        }
                    }
                } else {
                    if (!exp.getReturnType().toClass().isAssignableFrom(uti.bCls)) {
                        throw new IException("user type " + uti.userType.getClass() + " is not compatible with " + s + " in " + method);
                    }
                }
                params.add(exp);
                utis.add(uti);
                sb.append('?');
            }
        }
        if (end != sql.length()) {
            sb.append(Utils.strSrc(sql.substring(end)));
        }
        sql = sb.toString();
        code("java.sql.Connection _con = _pool.getConnection();");
        code("java.sql.PreparedStatement _stmt = null;");
        code("java.sql.ResultSet _rs = null;");
        code("try {");
        code("_stmt = _con.prepareStatement(\"" + sql + "\");");
        for (int i = 0; i < params.size(); i++) {
            Expression exp = params.get(i);
            UserTypeInfo uti = utis.get(i);
            if (uti == null) {
                Utils.setStmtParam(exp.getReturnType().toClass(), src, i + 1, exp.getEvalString());
            } else {
                String utv = userTypeMap.get(uti.userType);
                if (utv == null) userTypeMap.put(uti.userType, utv = "_ut" + seq++);
                Utils.setStmtParam(uti.dCls, src, i + 1, "(" + uti.dCls.getCanonicalName() + ")" + utv + ".beanToDB(" + exp.getEvalString() + ")");
            }
        }
    }
