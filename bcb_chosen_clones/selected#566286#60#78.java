    public Collection<HighlightElement> findMatches(Document document) {
        try {
            Collection<HighlightElement> highlightElements = new LinkedList<HighlightElement>();
            String docText = "";
            docText = document.getText(0, document.getLength());
            Matcher patternMatcher = pattern.matcher(docText);
            while (patternMatcher.find()) {
                String patternMatchingText = patternMatcher.group();
                for (String token : tokensToPaintInPattern) {
                    Pattern tokenPattern = Pattern.compile(token);
                    Matcher tokenMatcher = tokenPattern.matcher(patternMatchingText);
                    while (tokenMatcher.find()) highlightElements.add(new HighlightElement(patternMatcher.start() + tokenMatcher.start(), patternMatcher.start() + tokenMatcher.end(), painter));
                }
            }
            return highlightElements;
        } catch (BadLocationException ex) {
            throw new IllegalArgumentException("Malformed document: " + document, ex);
        }
    }
