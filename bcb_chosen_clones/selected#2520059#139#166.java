    private Collection<String> getVariants(String oldPatternText, String searchText, String replaceText) {
        Collection<String> variants = new HashSet<String>();
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(searchText, java.util.regex.Pattern.LITERAL);
        Matcher m = p.matcher(oldPatternText);
        List<MatchResult> results = new ArrayList<MatchResult>();
        while (m.find()) {
            results.add(m.toMatchResult());
        }
        int n = results.size();
        for (int patNum = 1; patNum < twoToThe(n); patNum++) {
            String newPatternText = new String(oldPatternText);
            int offset = 0;
            for (int matchNum = 0; matchNum < n; matchNum++) {
                if (isBitSet(patNum, matchNum)) {
                    MatchResult hit = results.get(matchNum);
                    int realStart = hit.start() + offset;
                    int realEnd = hit.end() + offset;
                    Matcher replacer = p.matcher(newPatternText);
                    newPatternText = replaceFirst(replacer, realStart, realEnd, replaceText);
                    int hitLength = hit.end() - hit.start();
                    int growth = replaceText.length() - hitLength;
                    offset += growth;
                    variants.add(newPatternText);
                }
            }
        }
        return variants;
    }
