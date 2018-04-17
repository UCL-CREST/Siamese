    public Expression inline(ApplyExp exp, InlineCalls walker, boolean argsInlined) {
        exp.walkArgs(walker, argsInlined);
        Compilation comp = walker.getCompilation();
        Expression[] args = exp.getArgs();
        int nargs = args.length;
        if (!comp.mustCompile || nargs == 0 || ((kind == 'V' || kind == '*') && nargs == 1)) return exp;
        ObjectType type;
        Expression arg0 = args[0];
        Type type0 = (kind == 'V' || kind == '*' ? arg0.getType() : language.getTypeFor(arg0));
        if (type0 instanceof PairClassType) type = ((PairClassType) type0).instanceType; else if (type0 instanceof ObjectType) type = (ObjectType) type0; else type = null;
        String name = getMethodName(args);
        int margsLength, argsStartIndex, objIndex;
        if (kind == 'V' || kind == '*') {
            margsLength = nargs - 1;
            argsStartIndex = 2;
            objIndex = 0;
        } else if (kind == 'N') {
            margsLength = nargs;
            argsStartIndex = 0;
            objIndex = -1;
        } else if (kind == 'S' || kind == 's') {
            margsLength = nargs - 2;
            argsStartIndex = 2;
            objIndex = -1;
        } else if (kind == 'P') {
            margsLength = nargs - 2;
            argsStartIndex = 3;
            objIndex = 1;
        } else return exp;
        if (kind == 'N' && type instanceof ArrayType) {
            ArrayType atype = (ArrayType) type;
            Type elementType = atype.getComponentType();
            Expression sizeArg = null;
            boolean lengthSpecified = false;
            if (args.length >= 3 && args[1] instanceof QuoteExp) {
                Object arg1 = ((QuoteExp) args[1]).getValue();
                if (arg1 instanceof Keyword && ("length".equals(name = ((Keyword) arg1).getName()) || "size".equals(name))) {
                    sizeArg = args[2];
                    lengthSpecified = true;
                }
            }
            if (sizeArg == null) sizeArg = QuoteExp.getInstance(new Integer(args.length - 1));
            Expression alloc = new ApplyExp(new ArrayNew(elementType), new Expression[] { sizeArg });
            if (lengthSpecified && args.length == 3) return alloc;
            LetExp let = new LetExp(new Expression[] { alloc });
            Declaration adecl = let.addDeclaration((String) null, atype);
            adecl.noteValue(alloc);
            BeginExp begin = new BeginExp();
            int index = 0;
            for (int i = lengthSpecified ? 3 : 1; i < args.length; i++) {
                Expression arg = args[i];
                if (lengthSpecified && i + 1 < args.length && arg instanceof QuoteExp) {
                    Object key = ((QuoteExp) arg).getValue();
                    if (key instanceof Keyword) {
                        String kname = ((Keyword) key).getName();
                        try {
                            index = Integer.parseInt(kname);
                            arg = args[++i];
                        } catch (Throwable ex) {
                            comp.error('e', "non-integer keyword '" + kname + "' in array constructor");
                            return exp;
                        }
                    }
                }
                begin.add(new ApplyExp(new ArraySet(elementType), new Expression[] { new ReferenceExp(adecl), QuoteExp.getInstance(new Integer(index)), arg }));
                index++;
            }
            begin.add(new ReferenceExp(adecl));
            let.body = begin;
            return let;
        } else if (type != null && name != null) {
            if (type instanceof TypeValue && kind == 'N') {
                Procedure constructor = ((TypeValue) type).getConstructor();
                if (constructor != null) {
                    Expression[] xargs = new Expression[nargs - 1];
                    System.arraycopy(args, 1, xargs, 0, nargs - 1);
                    return ((InlineCalls) walker).walkApplyOnly(new ApplyExp(constructor, xargs));
                }
            }
            PrimProcedure[] methods;
            int okCount, maybeCount;
            ClassType caller = comp == null ? null : comp.curClass != null ? comp.curClass : comp.mainClass;
            ObjectType ctype = (ObjectType) type;
            try {
                methods = getMethods(ctype, name, caller);
                long num = selectApplicable(methods, ctype, args, margsLength, argsStartIndex, objIndex);
                okCount = (int) (num >> 32);
                maybeCount = (int) num;
            } catch (Exception ex) {
                comp.error('w', "unknown class: " + type.getName());
                return exp;
            }
            int index = -1;
            Object[] slots;
            if (okCount + maybeCount == 0 && kind == 'N' && (ClassMethods.selectApplicable(methods, new Type[] { Compilation.typeClassType }) >> 32) == 1 && (slots = checkKeywords(type, args, 1, caller)) != null) {
                StringBuffer errbuf = null;
                for (int i = 0; i < slots.length; i++) {
                    if (slots[i] instanceof String) {
                        if (errbuf == null) {
                            errbuf = new StringBuffer();
                            errbuf.append("no field or setter ");
                        } else errbuf.append(", ");
                        errbuf.append('`');
                        errbuf.append(slots[i]);
                        errbuf.append('\'');
                    }
                }
                if (errbuf != null) {
                    errbuf.append(" in class ");
                    errbuf.append(type.getName());
                    comp.error('w', errbuf.toString());
                    return exp;
                } else {
                    ApplyExp e = new ApplyExp(methods[0], new Expression[] { arg0 });
                    for (int i = 0; i < slots.length; i++) {
                        Expression[] sargs = { e, new QuoteExp(slots[i]), args[2 * i + 2] };
                        e = new ApplyExp(SlotSet.setFieldReturnObject, sargs);
                    }
                    return e.setLine(exp);
                }
            }
            int nmethods = methods.length;
            if (okCount + maybeCount == 0 && kind == 'N') {
                methods = invokeStatic.getMethods(ctype, "valueOf", caller);
                argsStartIndex = 1;
                margsLength = nargs - 1;
                long num = selectApplicable(methods, ctype, args, margsLength, argsStartIndex, -1);
                okCount = (int) (num >> 32);
                maybeCount = (int) num;
            }
            if (okCount + maybeCount == 0) {
                if (kind == 'P' || comp.getBooleanOption("warn-invoke-unknown-method", true)) {
                    if (kind == 'N') name = name + "/valueOf";
                    String message = (nmethods + methods.length == 0 ? "no accessible method '" + name + "' in " + type.getName() : ("no possibly applicable method '" + name + "' in " + type.getName()));
                    comp.error(kind == 'P' ? 'e' : 'w', message);
                }
            } else if (okCount == 1 || (okCount == 0 && maybeCount == 1)) index = 0; else if (okCount > 0) {
                index = MethodProc.mostSpecific(methods, okCount);
                if (index < 0) {
                    if (kind == 'S') {
                        for (int i = 0; i < okCount; i++) {
                            if (methods[i].getStaticFlag()) {
                                if (index >= 0) {
                                    index = -1;
                                    break;
                                } else index = i;
                            }
                        }
                    }
                }
                if (index < 0 && (kind == 'P' || comp.getBooleanOption("warn-invoke-unknown-method", true))) {
                    StringBuffer sbuf = new StringBuffer();
                    sbuf.append("more than one definitely applicable method `");
                    sbuf.append(name);
                    sbuf.append("' in ");
                    sbuf.append(type.getName());
                    append(methods, okCount, sbuf);
                    comp.error(kind == 'P' ? 'e' : 'w', sbuf.toString());
                }
            } else if (kind == 'P' || comp.getBooleanOption("warn-invoke-unknown-method", true)) {
                StringBuffer sbuf = new StringBuffer();
                sbuf.append("more than one possibly applicable method '");
                sbuf.append(name);
                sbuf.append("' in ");
                sbuf.append(type.getName());
                append(methods, maybeCount, sbuf);
                comp.error(kind == 'P' ? 'e' : 'w', sbuf.toString());
            }
            if (index >= 0) {
                Expression[] margs = new Expression[margsLength];
                int dst = 0;
                if (objIndex >= 0) margs[dst++] = args[objIndex];
                for (int src = argsStartIndex; src < args.length && dst < margs.length; src++, dst++) margs[dst] = args[src];
                return new ApplyExp(methods[index], margs).setLine(exp);
            }
        }
        return exp;
    }
