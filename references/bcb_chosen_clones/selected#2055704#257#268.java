    public static Set getCode(String strMail) {
        Set set = new HashSet();
        Pattern p = null;
        Matcher m = null;
        p = Pattern.compile("1[3,5][4,5,6,7,8,9]\\d{8}|15[8,9]\\d{8}");
        m = p.matcher(strMail);
        while (m.find()) {
            String str = strMail.substring(m.start(), m.end());
            set.add(str);
        }
        return set;
    }
