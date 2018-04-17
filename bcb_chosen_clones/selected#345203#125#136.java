    public static List<String> findInString(String original, String regex) {
        List<String> matches = new ArrayList<String>();
        Pattern number = Pattern.compile(regex);
        Matcher matcher = number.matcher(original);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            if (start == end) continue;
            matches.add(original.substring(matcher.start(), matcher.end()));
        }
        return matches;
    }
