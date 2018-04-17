    public Collection tokenizeHTML() {
        List tokens = new ArrayList();
        String nestedTags = nestedTagsRegex(6);
        Pattern p = Pattern.compile("" + "(?s:<!(--.*?--\\s*)+>)" + "|" + "(?s:<\\?.*?\\?>)" + "|" + nestedTags + "", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(text);
        int lastPos = 0;
        while (m.find()) {
            if (lastPos < m.start()) {
                tokens.add(HTMLToken.text(text.substring(lastPos, m.start())));
            }
            tokens.add(HTMLToken.tag(text.substring(m.start(), m.end())));
            lastPos = m.end();
        }
        if (lastPos < text.length()) {
            tokens.add(HTMLToken.text(text.substring(lastPos, text.length())));
        }
        return tokens;
    }
