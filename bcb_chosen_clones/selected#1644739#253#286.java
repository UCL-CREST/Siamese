    private String expandText(String text) {
        Pattern pattern = Pattern.compile("[\\$,%]\\{([^\\}]+)\\}");
        Matcher matcher = pattern.matcher(text);
        int matchEnd = 0;
        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            String subText = text.substring(matchEnd, matcher.start());
            subText = subText.replaceAll("\\s+", " ");
            if (subText.length() > 0) {
                if (result.length() > 0) {
                    result.append('+');
                }
                result.append('"');
                result.append(subText);
                result.append('"');
            }
            if (result.length() > 0) {
                result.append('+');
            }
            result.append(matcher.group(1));
            matchEnd = matcher.end();
        }
        String subText = text.substring(matchEnd);
        subText = subText.replaceAll("\\s+", " ");
        if (subText.length() > 0) {
            if (result.length() > 0) {
                result.append('+');
            }
            result.append('"');
            result.append(subText);
            result.append('"');
        }
        return result.toString();
    }
