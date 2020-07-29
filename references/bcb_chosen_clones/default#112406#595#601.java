    private void highlightWord(String def, String word) throws BadLocationException {
        Pattern ptr = Pattern.compile(word);
        Matcher matcher = ptr.matcher(def);
        while (matcher.find()) {
            highlighter.addHighlight(matcher.start(), matcher.end(), highlightPainter);
        }
    }
