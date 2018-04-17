    private String matchPattern(String string, String regex, String tag) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);
        int lastend = 0;
        String output = "";
        while (matcher.find()) {
            output += string.substring(lastend, matcher.start(1));
            output += colorize(matcher, tag);
            lastend = matcher.end(1);
        }
        output += string.substring(lastend);
        return output;
    }
