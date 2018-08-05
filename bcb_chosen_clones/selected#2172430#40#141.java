    private Class compile0(String name, String source) throws CannotCompileException, NotFoundException {
        source = source.trim();
        Map<String, StringBuilder> smap = new HashMap<String, StringBuilder>();
        {
            StringBuilder sb = new StringBuilder();
            StringBuilder sv = null;
            for (int i = 0; i < source.length(); i++) {
                char c = source.charAt(i);
                if (c == '\\') {
                    (sv != null ? sv : sb).append('\\').append(source.charAt(++i));
                    continue;
                }
                if (c == '"') {
                    if (sv == null) {
                        String k = "[!-" + i + "-!]";
                        smap.put(k, sv = new StringBuilder().append(c));
                        sb.append(k);
                    } else {
                        sv.append(c);
                        sv = null;
                    }
                } else if (sv == null) {
                    sb.append(c);
                } else {
                    sv.append(c);
                }
            }
            source = sb.toString();
        }
        CtClass ctCls = classPool.makeClass("cn.webwheel.gen." + name);
        {
            String s = source.substring(0, source.indexOf('{'));
            int ei = s.indexOf("extends ");
            int ii = s.indexOf("implements ");
            String es = null;
            String is = null;
            if (ii == -1) {
                if (ei != -1) {
                    es = s.substring(ei + "extends ".length());
                }
            } else if (ei == -1) {
                if (ii != -1) {
                    is = s.substring(ii + "implements ".length());
                }
            } else if (ii > ei) {
                es = s.substring(ei + "extends ".length(), ii);
                is = s.substring(ii + "implements ".length());
            } else {
                is = s.substring(ii + "implements ".length(), ei);
                es = s.substring(ei + "extends ".length());
            }
            if (es != null) {
                ctCls.setSuperclass(classPool.get(es.trim()));
            }
            if (is != null) {
                String[] ss = is.split(",");
                CtClass[] cs = new CtClass[ss.length];
                for (int i = 0; i < ss.length; i++) {
                    cs[i] = classPool.get(ss[i].trim());
                }
                ctCls.setInterfaces(cs);
            }
            source = source.substring(source.indexOf('{') + 1, source.length() - 1);
        }
        {
            StringBuilder sb = new StringBuilder();
            int i = 0;
            int j = 0;
            Pattern pat = Pattern.compile("\\[!\\-(\\d+)\\-!\\]");
            List<String> ms = new ArrayList<String>();
            for (; i < source.length(); i++) {
                char c = source.charAt(i);
                sb.append(c);
                if (c == '{') {
                    j++;
                } else if (c == '}') {
                    j--;
                    if (j == 0) {
                        String s = sb.toString();
                        Matcher matcher = pat.matcher(s);
                        sb = new StringBuilder();
                        int end = 0;
                        while (matcher.find()) {
                            sb.append(s.substring(end, matcher.start()));
                            sb.append(smap.get(matcher.group()));
                            end = matcher.end();
                        }
                        sb.append(s.substring(end));
                        ms.add(sb.toString());
                        sb = new StringBuilder();
                    }
                } else if (c == ';' && j == 0) {
                    ctCls.addField(CtField.make(sb.toString(), ctCls));
                    sb = new StringBuilder();
                }
            }
            for (String m : ms) {
                ctCls.addMethod(CtMethod.make(m, ctCls));
            }
        }
        return ctCls.toClass(classLoader, null);
    }
