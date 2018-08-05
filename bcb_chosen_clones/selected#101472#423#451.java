    public static String getStackTraceFromDescription(String description) {
        String stackTrace = null;
        if (description == null) {
            return null;
        }
        String punct = "!\"#$%&'\\(\\)*+,-./:;\\<=\\>?@\\[\\]^_`\\{|\\}~\n";
        String lineRegex = " *at\\s+[\\w" + punct + "]+ ?\\(.*\\) *\n?";
        Pattern tracePattern = Pattern.compile(lineRegex);
        Matcher match = tracePattern.matcher(description);
        if (match.find()) {
            int start = match.start();
            int lastEnd = match.end();
            while (match.find()) {
                lastEnd = match.end();
            }
            if (start <= 0) {
                return null;
            }
            int stackStart = 0;
            int index = start - 1;
            while (index > 1 && description.charAt(index) == ' ') {
                index--;
            }
            stackStart = description.substring(0, index - 1).lastIndexOf("\n");
            stackStart = (stackStart == -1) ? 0 : stackStart + 1;
            stackTrace = description.substring(stackStart, lastEnd);
        }
        return stackTrace;
    }
