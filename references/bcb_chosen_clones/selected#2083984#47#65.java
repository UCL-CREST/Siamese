    public static int strspn(String s, CharSequence f) {
        String regex = "";
        int res = 0;
        for (int i = 0; i < f.length(); i++) {
            regex += "|" + f.charAt(i);
        }
        regex = regex.substring(1);
        regex = "(" + regex + ")+";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(s);
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            if (res < (end - start)) {
                res = (end - start);
            }
        }
        return res;
    }
