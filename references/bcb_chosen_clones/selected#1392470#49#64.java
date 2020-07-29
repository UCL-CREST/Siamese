    @Constraint("post: s.get().size>0")
    protected static void removeNewLinesForPREDefinition(@Constraint("post: s.get().size<=s@pre.get().size") final IDocument s) {
        Pattern pattern = Pattern.compile("java.util.Map<String, tudresden.ocl.lib.OclRoot> PRE = new java.util.HashMap<String, tudresden.ocl.lib.OclRoot>();\n", Pattern.LITERAL);
        Matcher matcher = pattern.matcher(s.get());
        try {
            int lengthDifference = 0;
            while (matcher.find()) {
                String replacement = "java.util.Map<String, tudresden.ocl.lib.OclRoot> PRE = new java.util.HashMap<String, tudresden.ocl.lib.OclRoot>();";
                int lengthOfMatchedString = matcher.end() - matcher.start();
                s.replace(lengthDifference + matcher.start(), lengthOfMatchedString, replacement);
                lengthDifference -= lengthOfMatchedString - replacement.length();
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
