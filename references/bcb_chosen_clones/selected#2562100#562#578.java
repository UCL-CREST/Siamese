    private ArrayList<Point> regexSearch(final String text, final String string, final boolean caseSensitive) {
        int loc = 0;
        Point match;
        int caseFlag = 0;
        if (!caseSensitive) {
            caseFlag = Pattern.CASE_INSENSITIVE;
        }
        Pattern pattern = Pattern.compile(string, caseFlag);
        Matcher matcher = pattern.matcher(Matcher.quoteReplacement(text));
        ArrayList<Point> results = new ArrayList<Point>();
        while (matcher.find(loc)) {
            match = new Point(matcher.start(), matcher.end());
            results.add(match);
            loc = match.x + 1;
        }
        return results;
    }
