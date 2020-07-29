
    public static int[] findLastRegExp2(String input, String regExp) throws java.util.regex.PatternSyntaxException {
        int[] out = new int[2];
        out[0] = -1;
        Pattern lbPattern = Pattern.compile(regExp);
        Matcher matcher = lbPattern.matcher(input);
        while (matcher.find()) {
            out[0] = matcher.start();
            out[1] = matcher.end();
        }
        return out;
