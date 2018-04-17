    public static String unescapeStringForXML(String s) {
        Pattern p = Pattern.compile("\\&.+?;");
        StringBuilder result = new StringBuilder();
        Matcher m = p.matcher(s);
        int end = 0;
        while (m.find()) {
            int start = m.start();
            result.append(s.substring(end, start));
            end = m.end();
            result.append(translate(s.substring(start, end)));
        }
        result.append(s.substring(end, s.length()));
        return result.toString();
    }
