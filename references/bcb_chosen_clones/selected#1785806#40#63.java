    public static String replaceTemplateVariables(String text, Map<String, String> replacements) {
        StringBuilder output = new StringBuilder();
        Pattern tokenPattern = Pattern.compile("\\{([^}]+)\\}");
        Matcher tokenMatcher = tokenPattern.matcher(text);
        int cursor = 0;
        while (tokenMatcher.find()) {
            int tokenStart = tokenMatcher.start();
            int tokenEnd = tokenMatcher.end();
            int keyStart = tokenMatcher.start(1);
            int keyEnd = tokenMatcher.end(1);
            output.append(text.substring(cursor, tokenStart));
            String token = text.substring(tokenStart, tokenEnd);
            String key = text.substring(keyStart, keyEnd);
            if (replacements.containsKey(key)) {
                String value = replacements.get(key);
                output.append(value);
            } else {
                output.append(token);
            }
            cursor = tokenEnd;
        }
        output.append(text.substring(cursor));
        return output.toString();
    }
