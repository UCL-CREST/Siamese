    protected String replaceValue(String text, Map map) {
        Pattern pattern = Pattern.compile("\\#\\{[a-zA-Z_]+\\}", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(text);
        int beforeIndex = 0;
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            int startIndex = matcher.start();
            if (beforeIndex < startIndex) {
                String subText = text.substring(beforeIndex, startIndex);
                buffer.append(subText);
            }
            String dollText = text.substring(startIndex, matcher.end());
            String valueName = dollText.substring(2, dollText.length() - 1);
            Object value = map.get(valueName);
            if (value == null) {
                value = "";
            }
            buffer.append(value.toString());
            beforeIndex = matcher.end();
        }
        if (beforeIndex < text.length()) {
            String lastText = text.substring(beforeIndex);
            buffer.append(lastText);
        }
        return buffer.toString();
    }
