    public static List<String> tokenizeRegexp(String str) {
        Pattern pattern = Pattern.compile("\".*?\"");
        Matcher matcher = pattern.matcher(str);
        List<String> tokens = new ArrayList<String>();
        int start = 0;
        while (matcher.find()) {
            tokens.addAll(Arrays.asList(str.substring(start, matcher.start()).trim().split(" ")));
            tokens.add(matcher.group().trim());
            start = matcher.end();
        }
        tokens.addAll(Arrays.asList(str.substring(start).trim().split(" ")));
        return tokens;
    }
