    static final java.util.HashMap<String, String> parseSessionString(String str) throws java.text.ParseException {
        java.util.HashMap<String, String> strh = new java.util.HashMap<String, String>();
        java.util.ArrayList<String> stra = new java.util.ArrayList<String>();
        final String regex1 = "[^\\\\][ \\t]+";
        final String regex2 = "[^\\\\]=";
        final java.util.regex.Pattern p1 = java.util.regex.Pattern.compile(regex1);
        final java.util.regex.Pattern p2 = java.util.regex.Pattern.compile(regex2);
        final java.util.regex.Matcher m1 = p1.matcher(str);
        final String err = "SessionCmd(parser): ";
        final String err_ic = err + "Illegal Command (must not contain '=')";
        int prev = 0;
        while (m1.find()) {
            int a = m1.start();
            int b = m1.end() - 1;
            String s = str.substring(prev, a + 1);
            stra.add(s);
            prev = m1.end();
        }
        if (prev != str.length()) {
            stra.add(str.substring(prev, str.length()));
        }
        boolean cSet = false;
        boolean nSet = false;
        boolean oSet = false;
        boolean bSet = false;
        String KeyOrValue = null;
        for (String s : stra) {
            String prompt;
            if (s.equals("=")) {
                if (!cSet) {
                    throw new java.text.ParseException(err_ic, 0);
                }
                if (oSet) {
                    throw new java.text.ParseException(err + "detected '=' '=', but need '=' 'value'", 0);
                }
                if (bSet) {
                    throw new java.text.ParseException(err + "detected 'key=' '=', but need 'key=' 'value'", 0);
                }
                if (nSet) {
                    oSet = true;
                } else {
                    throw new java.text.ParseException(err + "detected '=', but no 'key' set", 0);
                }
                prompt = "O:";
            } else if (s.matches(".*[^\\\\]=")) {
                if (!cSet) {
                    throw new java.text.ParseException(err_ic, 0);
                }
                if (oSet) {
                    throw new java.text.ParseException(err + "detected '=' 'key=', but need '=' 'value'", 0);
                }
                if (bSet) {
                    throw new java.text.ParseException(err + "detected 'key=' 'key=', but need 'key=' 'value'", 0);
                }
                if (nSet) {
                    strh.put(unEscape(KeyOrValue), "true");
                }
                bSet = true;
                KeyOrValue = s.substring(0, s.length() - 1);
                prompt = "B:";
            } else if (s.matches("=..*")) {
                if (!cSet) {
                    throw new java.text.ParseException(err_ic, 0);
                }
                if (oSet) {
                    throw new java.text.ParseException(err + "detected '=' '=value', but need '=' 'value'", 0);
                }
                if (bSet) {
                    throw new java.text.ParseException(err + "detected 'key=' '=value', but need 'key=' 'value'", 0);
                }
                if (nSet) {
                    strh.put(unEscape(KeyOrValue), unEscape(s.substring(1)));
                    nSet = false;
                } else {
                    throw new java.text.ParseException(err + "detected '=value', but no 'key' set", 0);
                }
                prompt = "E:";
            } else if (s.matches(".*[^\\\\]=..*")) {
                if (!cSet) {
                    throw new java.text.ParseException(err_ic, 0);
                }
                if (oSet) {
                    throw new java.text.ParseException(err + "detected '=' 'key=value', but need '=' 'value'", 0);
                }
                if (bSet) {
                    throw new java.text.ParseException(err + "detected 'key=' 'key=value', but need 'key=' 'value'", 0);
                }
                if (nSet) {
                    strh.put(unEscape(KeyOrValue), "true");
                    nSet = false;
                }
                final java.util.regex.Matcher m2 = p2.matcher(s);
                m2.find();
                strh.put(unEscape(s.substring(0, m2.start() + 1)), unEscape(s.substring(m2.end(), s.length())));
                prompt = "m:";
            } else {
                if (!cSet) {
                    prompt = "C:";
                    strh.put(null, unEscape(s));
                    cSet = true;
                } else {
                    prompt = "N:";
                    if (nSet && oSet) {
                        strh.put(unEscape(KeyOrValue), unEscape(s));
                        nSet = false;
                        oSet = false;
                    } else if (bSet) {
                        strh.put(unEscape(KeyOrValue), unEscape(s));
                        bSet = false;
                    } else if (nSet) {
                        strh.put(unEscape(KeyOrValue), "true");
                        KeyOrValue = s;
                    } else {
                        nSet = true;
                        KeyOrValue = s;
                    }
                }
            }
        }
        if (bSet) {
            throw new java.text.ParseException(err + "detected 'key=', but no 'value'", 0);
        } else if (nSet && oSet) {
            throw new java.text.ParseException(err + "detected 'key' '=', but no 'value'", 0);
        } else if (nSet) {
            strh.put(unEscape(KeyOrValue), "true");
        }
        return strh;
    }
