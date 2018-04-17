    public static List<String> matchesToList(String pattern, String input) {
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(input);
        List<String> results = new ArrayList<String>();
        while (matcher.find()) {
            results.add(input.substring(matcher.start(), matcher.end()));
        }
        return results;
    }
