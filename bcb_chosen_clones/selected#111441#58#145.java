    public static String update(String e1) {
        String s = "";
        String ex = "";
        String var = "";
        String t = e1;
        int i = 0;
        while (i < e1.length()) {
            if (t.indexOf('#') < 0) {
                if (i < 1) {
                    s = e1;
                }
                if (s.indexOf('#') > -1) {
                    s = update(s.replaceAll("@", "@;"));
                    s = s.replaceAll(";", "");
                }
                break;
            } else {
                if (e1.charAt(i) == '#') {
                    String s1 = e1.substring(i);
                    int j = s1.indexOf('@');
                    if (j > -1) {
                        if (s1.charAt(j + 1) == '(') {
                            ex = s1.substring(j + 1, s1.indexOf(')'));
                        } else {
                            if (s1.charAt(j + 1) == '#' || s1.charAt(j + 1) == ';') {
                                String ss = s1.substring(j + 1);
                                if (ss.indexOf(')') > 0) {
                                    ex = ss.substring(0, ss.indexOf(")"));
                                } else {
                                    ex = s1.substring(j + 1, s1.length());
                                }
                            } else {
                                ex = s1.substring(j + 1, j + 2);
                            }
                        }
                    }
                    int k = s1.indexOf('.');
                    if (k > -1) {
                        var = s1.substring(1, k);
                    }
                    if (t.charAt(0) != '#') {
                        s = s + t.substring(0, t.indexOf("#"));
                    }
                    if (t.indexOf("@" + ex) + ex.length() + 1 < t.length()) {
                        t = t.substring(t.indexOf("@" + ex) + ex.length() + 1);
                    } else {
                        t = "";
                    }
                    if (j > -1) {
                        s1 = s1.substring(k + 1, j);
                    } else s1 = s1.substring(k + 1);
                    if (s.indexOf(".") > 0) {
                        s = s.substring(0, s.indexOf("#"));
                        s = s + s1.replaceAll(var, ex);
                    } else {
                        s = s + s1.replaceAll(var, ex);
                    }
                    int l = e1.length();
                    e1 = s + t;
                    i -= l - e1.length();
                    if (i < 0) {
                        i = 0;
                    }
                }
            }
            i++;
        }
        String mul = s + t;
        Pattern p = Pattern.compile("[([\\d]+)\\*]+([\\d]+)");
        Matcher m = p.matcher(e1);
        while (m.find()) {
            String occur = m.group();
            int start = m.start();
            int end = m.end();
            String[] muls = occur.split("\\*");
            int result = 1;
            for (int j = 0; j < muls.length; j++) {
                try {
                    int cur = Integer.parseInt(muls[j]);
                    result = result * cur;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            mul = mul.substring(0, start) + Integer.toString(result) + mul.substring(end, mul.length());
        }
        return mul;
    }
