    public static String convertJS(String source) {
        if (source == null) return null;
        Pattern ptn = Pattern.compile("\\$\\{[^}]+\\}");
        Matcher m = ptn.matcher(source);
        char[] charArray = source.toCharArray();
        while (m.find()) {
            Arrays.fill(charArray, m.start(), m.end(), '1');
        }
        return new String(charArray);
    }
