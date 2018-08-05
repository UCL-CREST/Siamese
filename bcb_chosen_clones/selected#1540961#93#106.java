    public int getParamIndex(String sql, String parameter) {
        String pat = "\\{[a-z0-9_]+\\}";
        Pattern p = Pattern.compile(pat);
        Matcher m = p.matcher(sql);
        int s = 0;
        int i = 1;
        while (m.find(s)) {
            s = m.start();
            if (sql.substring(s, s + parameter.length()).equals(parameter)) return i;
            s = m.end();
            i++;
        }
        throw new RuntimeException("Failed to find parameter " + parameter + " in sql " + sql);
    }
