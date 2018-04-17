    private String escape(String content) {
        if (content == null || content.length() == 0) {
            return "";
        }
        Pattern p = Pattern.compile(ESCAPE_START_TAG + ".+?" + ESCAPE_END_TAG, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher m = p.matcher(content);
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            String textToEscape = content.substring(start, end);
            textToEscape = textToEscape.substring(ESCAPE_START_TAG.length(), textToEscape.length() - ESCAPE_END_TAG.length());
            textToEscape = StringUtils.transformHTML(textToEscape);
            content = content.substring(0, start) + textToEscape + content.substring(end, content.length());
            m = p.matcher(content);
        }
        return content;
    }
