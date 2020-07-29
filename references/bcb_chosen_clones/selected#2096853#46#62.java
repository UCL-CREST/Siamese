    public static String replaceAll(String regExp, String input, String replacement, int flags) {
        Pattern pattern = Pattern.compile(regExp, flags);
        Matcher matcher = pattern.matcher(input);
        String returnString = "";
        int start = 0;
        int end = 0;
        while (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                end = matcher.start(i);
                returnString = returnString + input.substring(start, end);
                returnString = returnString + replacement;
                start = matcher.end(i);
            }
        }
        returnString = returnString + input.substring(start);
        return returnString;
    }
