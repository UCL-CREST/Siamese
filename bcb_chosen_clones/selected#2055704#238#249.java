    public static Set getTextBlock(String text, String compile) {
        Set set = new HashSet();
        Pattern p = null;
        Matcher m = null;
        p = Pattern.compile(compile);
        m = p.matcher(text);
        while (m.find()) {
            String str = text.substring(m.start(), m.end());
            set.add(str);
        }
        return set;
    }
