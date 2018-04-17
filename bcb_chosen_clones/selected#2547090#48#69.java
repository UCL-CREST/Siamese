    private String parseTags(String string) {
        string = string.replaceAll("&lt;/(.+?)&gt;", "<span class=\"textag\">$0</span>");
        Pattern pattern = Pattern.compile("(?s)(?i)(&lt;\\w.*?)(&nbsp;(?:.*?))?(/?&gt;)");
        Matcher matcher = pattern.matcher(string);
        String output = "";
        int lastend = 0;
        while (matcher.find()) {
            output += string.substring(lastend, matcher.start(1));
            output += "<span class=\"textag\">";
            output += matcher.group(1);
            if (matcher.group(2) != null) {
                output += "</span>";
                output += parseAttribs(matcher.group(2));
                output += "<span class=\"textag\">";
            }
            output += matcher.group(3);
            output += "</span>";
            lastend = matcher.end(3);
        }
        output += string.substring(lastend);
        return output;
    }
