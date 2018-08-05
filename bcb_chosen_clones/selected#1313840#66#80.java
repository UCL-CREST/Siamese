    public void initParser() {
        Pattern tokenPattern = Pattern.compile("\\$\\{\\w+\\}");
        String pattern = patternPanel.getValue();
        Matcher tokenMatcher = tokenPattern.matcher(pattern);
        separators = new LinkedList<String>();
        Integer lastEnd = null;
        while (tokenMatcher.find()) {
            String match = tokenMatcher.group();
            if (AUTHOR_TAG.equals(match) || ALBUM_TAG.equals(match) || TITLE_TAG.equals(match) || NUMBER_TAG.equals(match) || YEAR_TAG.equals(match)) {
                if (lastEnd != null) separators.add(pattern.substring(lastEnd, tokenMatcher.start()));
                lastEnd = tokenMatcher.end();
            }
        }
        if (lastEnd < pattern.length()) separators.add(pattern.substring(lastEnd)); else separators.add("\n");
    }
