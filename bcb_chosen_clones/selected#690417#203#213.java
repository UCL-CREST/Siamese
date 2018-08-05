    public static List<StringFragment> getLineFragmentation(String text) {
        List<StringFragment> result = new ArrayList<StringFragment>();
        Pattern pattern = Pattern.compile("\\r?\\n");
        Matcher m = pattern.matcher(text);
        int lastIndex = 0;
        while (m.find()) {
            result.add(new StringFragment(text.substring(lastIndex, m.start()), lastIndex, text));
            lastIndex = m.end();
        }
        return result;
    }
