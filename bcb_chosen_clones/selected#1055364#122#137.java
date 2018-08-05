    public String escapeString(String s) {
        Pattern pattern = Pattern.compile("\\W");
        Matcher matcher = pattern.matcher(s);
        StringBuffer escapedString = new StringBuffer();
        int previousEnd = 0;
        while (matcher.find()) {
            escapedString.append(s.substring(previousEnd, matcher.start()));
            escapedString.append("\\");
            escapedString.append(matcher.group());
            previousEnd = matcher.end();
        }
        if (previousEnd != s.length()) {
            escapedString.append(s.substring(previousEnd, s.length()));
        }
        return escapedString.toString();
    }
