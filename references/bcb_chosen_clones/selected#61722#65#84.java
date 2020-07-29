    private String parseTags(String string) {
        string = string.replaceAll("</(.+?)>", "\\\\textag $0");
        Pattern pattern = Pattern.compile("(?s)(?i)(<\\w.*?)(~(?:.*?))?(/?>)");
        Matcher matcher = pattern.matcher(string);
        String output = "";
        int lastend = 0;
        while (matcher.find()) {
            output += string.substring(lastend, matcher.start(1));
            output += "\\textag ";
            output += matcher.group(1);
            if (matcher.group(2) != null) {
                output += parseAttribs(matcher.group(2));
                output += "\\textag ";
            }
            output += matcher.group(3);
            lastend = matcher.end(3);
        }
        output += string.substring(lastend);
        return output;
    }
