    private static List<String> findParamValueArray(String paramString) {
        List<String> l = new ArrayList<String>();
        try {
            Pattern Regex = Pattern.compile("(, )?(\\(String\\) |\\(Timestamp\\) |\\(int\\) |\\(long\\) |\\(BigDecimal\\) )", Pattern.CANON_EQ);
            Matcher RegexMatcher = Regex.matcher(paramString);
            int count = 0;
            int start = 0;
            int end = 0;
            String currentType = null;
            while (RegexMatcher.find()) {
                if (count > 0) {
                    end = RegexMatcher.start();
                    String unformattedParam = paramString.substring(start, end);
                    l.add(getFormattedParamValue(unformattedParam, currentType));
                }
                start = RegexMatcher.end();
                currentType = RegexMatcher.group(2);
                count++;
            }
            String unformattedParam = paramString.substring(start, paramString.length());
            l.add(getFormattedParamValue(unformattedParam, currentType));
        } catch (PatternSyntaxException ex) {
            throw new RuntimeException("Tipo non supportato");
        }
        return l;
    }
