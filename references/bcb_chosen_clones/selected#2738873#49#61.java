    protected void loadRegex(String documentId, String string, String regexPattern) {
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(string);
        Document document = new Document(documentId, string);
        List tokenList = new ArrayList();
        while (matcher.find()) {
            tokenList.add(new TextToken(document, matcher.start(1), matcher.end(1) - matcher.start(1)));
        }
        if (tokenList.size() == 0) log.warn("empty document with id " + documentId);
        TextToken[] tokenArray = (TextToken[]) tokenList.toArray(new TextToken[0]);
        document.setTokens(tokenArray);
        documentMap.put(documentId, document);
    }
