    public static Set getMail(String content) {
        Set set = new HashSet();
        Pattern p = null;
        Matcher m = null;
        p = Pattern.compile("(?i)(?<=\\b)[a-z0-9][-a-z0-9_.]+[a-z0-9]@([a-z0-9][-a-z0-9]+\\.)+[a-z]{2,4}(?=\\b)");
        m = p.matcher(content);
        while (m.find()) {
            String str = content.substring(m.start(), m.end());
            set.add(str);
        }
        return set;
    }
