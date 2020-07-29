    protected static void assertBugInUnGeneratorWorkAround(final IDocument source) {
        Pattern pattern = Pattern.compile("assert\\s*assert(Pre|Post)Condition_(\\w*)_for_method_(\\w*)(\\s*)\\(([^\\)]*)\\)(\\s*):" + "(\\s*)\"([^\"]*|\\\\\")*\";");
        Matcher matcher = pattern.matcher(source.get());
        try {
            int lengthDifference = 0;
            while (matcher.find()) {
                String replacement = "";
                int lengthOfMatchedString = matcher.end() - matcher.start();
                source.replace(lengthDifference + matcher.start(), lengthOfMatchedString, replacement);
                lengthDifference -= lengthOfMatchedString - replacement.length();
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
