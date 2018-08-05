    private String parseAttribs(String string) {
        Pattern pattern = Pattern.compile("(?s)(?i)(\\w+)=\"(.*?)\"");
        Matcher matcher = pattern.matcher(string);
        int lastend = 0;
        String output = "";
        while (matcher.find()) {
            output += string.substring(lastend, matcher.start());
            output += "\\texattrib ";
            output += matcher.group(1) + "=";
            output += "\\texvalue \"" + matcher.group(2) + "\"";
            lastend = matcher.end();
        }
        output += string.substring(lastend);
        return output;
    }
