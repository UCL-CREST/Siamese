    private void highlightWordToBeAnonymized(String patternText) {
        if (highlightSuggested) {
            removeSuggestedHighlights();
        }
        try {
            Document document = transcript.getDocument();
            Pattern pattern = Pattern.compile(patternText, Pattern.MULTILINE);
            Matcher textMatcher = pattern.matcher(document.getText(0, document.getLength()));
            while (textMatcher.find()) {
                transcript.getHighlighter().addHighlight(textMatcher.start(), textMatcher.end() - 1, markedForAnonHighlighter);
            }
        } catch (BadLocationException error) {
            ErrorLog.instance().addEntry(error);
        }
        if (highlightSuggested) {
            highlightSuggestedWords();
        }
    }
