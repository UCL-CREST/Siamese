    protected void highlightText(StyledDocument doc) {
        super.highlightText(doc);
        for (String search : searches) {
            Style searchHighlight = doc.addStyle("RESULT_" + search, null);
            StyleConstants.setBold(searchHighlight, true);
            StyleConstants.setBackground(searchHighlight, colourMap.get(search));
            Pattern p = Pattern.compile("(?s)(?i)" + search);
            try {
                Matcher matcher = p.matcher(doc.getText(0, doc.getLength()));
                while (matcher.find()) {
                    final int start = matcher.start();
                    final int end = matcher.end();
                    doc.setCharacterAttributes(start, end - start, searchHighlight, false);
                }
            } catch (BadLocationException e) {
                throw new RuntimeException(e);
            }
        }
    }
