    public static String replaceTokens(String text, Map<String, String> values) {
        Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}");
        Matcher matcher = pattern.matcher(text);
        StringBuilder builder = new StringBuilder();
        int i = 0;
        while (matcher.find()) {
            String replacement = values.get(matcher.group(1));
            builder.append(text.substring(i, matcher.start()));
            if (replacement == null) builder.append(matcher.group(0)); else builder.append(replacement);
            i = matcher.end();
        }
        builder.append(text.substring(i, text.length()));
        return builder.toString();
    }
