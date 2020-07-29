    @Constraint("post: s.get().size>0")
    protected static void removeNewlinesForAssertStyleCommonsLogging(@Constraint("post: s.get().size<=s@pre.get().size") final IDocument s) {
        Pattern pattern = Pattern.compile("if\\s*\\(\\s*!\\s*assert(Pre|Post)Condition_(\\w*)_for_method_(\\w*)\\s*\\(([^\\)]*)\\)\\s*\\)\\s*\\{" + "\\s*org.apache.commons.logging.LogFactory.getLog\\s*\\(\\s*this.getClass\\s*\\(\\s*\\)\\s*\\)\\s*.\\s*(\\w*)\\s*\\(\\s*\"(.*)\"\\s*\\)\\s*;" + "\\s*}\\s*");
        Matcher matcher = pattern.matcher(s.get());
        try {
            int lengthDifference = 0;
            while (matcher.find()) {
                String replacement = "if(!assert" + Matcher.quoteReplacement(matcher.group(1)) + "Condition_" + Matcher.quoteReplacement(matcher.group(2)) + "_for_method_" + Matcher.quoteReplacement(matcher.group(3)) + "(" + Matcher.quoteReplacement(matcher.group(4)) + ")){org.apache.commons.logging.LogFactory.getLog(this.getClass())." + Matcher.quoteReplacement(matcher.group(5)) + "(\"" + Matcher.quoteReplacement(matcher.group(6)) + "\");}";
                int lengthOfMatchedString = matcher.end() - matcher.start();
                s.replace(lengthDifference + matcher.start(), lengthOfMatchedString, replacement);
                lengthDifference -= lengthOfMatchedString - replacement.length();
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
