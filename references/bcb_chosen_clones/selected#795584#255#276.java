    private String highlightQueryResult(String text, String query) {
        StringTokenizer tokenizer = new StringTokenizer(text, "; .,\n\r[](){}?!/|:'<>", true);
        StringBuilder result = new StringBuilder();
        while (tokenizer.hasMoreElements()) {
            String token = tokenizer.nextToken();
            if (token.toLowerCase().contains(query.toLowerCase())) {
                Pattern p;
                if (getCaseSensitive()) {
                    p = Pattern.compile(query);
                } else {
                    p = Pattern.compile(query, Pattern.CASE_INSENSITIVE);
                }
                Matcher m = p.matcher(token);
                while (m.find()) {
                    result.append(token.substring(0, m.start()) + "<strong>" + token.substring(m.start(), m.end()) + "</strong>" + token.substring(m.end(), token.length()));
                }
            } else {
                result.append(token);
            }
        }
        return result.toString();
    }
