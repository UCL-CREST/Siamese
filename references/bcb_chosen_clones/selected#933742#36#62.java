    public static int[][] find(String text, String term, boolean caseInsensitive, boolean fuzzy) {
        List<int[]> beginEnds = new ArrayList<int[]>();
        String regexp = "";
        if (fuzzy) {
            String[] termArray = term.split("\\s+");
            for (String termElement : termArray) {
                if (!regexp.isEmpty()) {
                    regexp += "[\\W|_]*";
                }
                regexp += Pattern.quote(termElement);
            }
        } else {
            regexp = Pattern.quote(term);
        }
        Pattern pattern;
        if (caseInsensitive) {
            pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE);
        } else {
            pattern = Pattern.compile(regexp);
        }
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            int[] beginEnd = new int[] { matcher.start(), matcher.end() };
            beginEnds.add(beginEnd);
        }
        return beginEnds.toArray(new int[0][0]);
    }
