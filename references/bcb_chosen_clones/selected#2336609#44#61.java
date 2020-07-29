    private String replaceQuotedStrings(String query) {
        StringBuffer buff = new StringBuffer(query);
        Pattern pattern = Pattern.compile(REGEX_QUOTED_STRING);
        Matcher matcher = pattern.matcher(buff);
        boolean found = false;
        int i = 0;
        while (matcher.find()) {
            quotedStrings.add(matcher.group());
            buff.replace(matcher.start(), matcher.end(), "{" + i + "}");
            matcher.reset();
            found = true;
            i++;
        }
        if (!found) {
        }
        String ret = buff.toString();
        return ret;
    }
