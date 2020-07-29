    @Constraint("post: s.get().size>0")
    protected static void removeNewlinesForAssertStyleSystemErr(@Constraint("post: s.get().size<=s@pre.get().size") final IDocument s) {
        Pattern pattern = Pattern.compile("if\\s*\\(\\s*!\\s*assert(Pre|Post)Condition_(\\w*)_for_method_(\\w*)\\s*\\(([^\\)]*)\\)\\s*\\)\\s*\\{" + "\\s*System.err.println\\(\\s*\"(.*)\"\\s*\\)\\s*;" + "\\s*}\\s*");
        Matcher matcher = pattern.matcher(s.get());
        try {
            int lengthDifference = 0;
            while (matcher.find()) {
                String replacement = "if(!assert" + Matcher.quoteReplacement(matcher.group(1)) + "Condition_" + Matcher.quoteReplacement(matcher.group(2)) + "_for_method_" + Matcher.quoteReplacement(matcher.group(3)) + "(" + Matcher.quoteReplacement(matcher.group(4)) + ")){System.err.println(\"" + Matcher.quoteReplacement(matcher.group(5)) + "\");}";
                int lengthOfMatchedString = matcher.end() - matcher.start();
                s.replace(lengthDifference + matcher.start(), lengthOfMatchedString, replacement);
                lengthDifference -= lengthOfMatchedString - replacement.length();
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
