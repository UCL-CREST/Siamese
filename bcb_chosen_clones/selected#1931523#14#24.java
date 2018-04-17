    public static String[] extractByPattern(String str, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(str);
        ArrayList<String> list = new ArrayList<String>(20);
        while (m.find()) {
            list.add(str.substring(m.start(), m.end()));
        }
        if (!list.isEmpty()) {
            return list.toArray(new String[0]);
        } else return null;
    }
