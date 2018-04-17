    public static List<String> extract(String s, String regExp) {
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(s);
        List<String> result = new ArrayList<String>();
        while (m.find()) {
            result.add(s.substring(m.start(1), m.end(1)));
        }
        return result;
    }
