    void spellCheck() {
        Highlighter hi = textComp.getHighlighter();
        hi.removeAllHighlights();
        List<String> words = parseText(textComp.getText());
        List<String> misspelledWords = spellCheck(words);
        if (misspelledWords.isEmpty()) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (String word : misspelledWords) {
            sb.append(word).append("|");
        }
        sb.setLength(sb.length() - 1);
        String patternStr = "\\b(" + sb.toString() + ")\\b";
        Pattern pattern = Pattern.compile(patternStr, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(textComp.getText());
        while (matcher.find()) {
            try {
                hi.addHighlight(matcher.start(), matcher.end(), myPainter);
            } catch (BadLocationException ex) {
            }
        }
    }
