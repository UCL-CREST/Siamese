    public boolean highlightText(String searchText, int searchType) {
        int firstIndex = -1;
        try {
            Document document = eventDetails.getDocument();
            String sourceText = document.getText(0, document.getLength()).toUpperCase();
            LinkedList matchAttributeBoundaryList = new LinkedList();
            constructMatchAttributeBoundaryList(sourceText, matchAttributeBoundaryList, searchType);
            Highlighter highlighter = eventDetails.getHighlighter();
            highlighter.removeAllHighlights();
            Highlighter.HighlightPainter highlightPainter = new DefaultHighlighter.DefaultHighlightPainter(UIManager.getDefaults().getColor(InsightConstants.TEXT_HIGHLIGHT));
            Pattern pattern = Pattern.compile(searchText, Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE | Pattern.CANON_EQ | Pattern.UNICODE_CASE);
            Matcher matcher = pattern.matcher(sourceText);
            while (matcher.find()) {
                int searchIndex = matcher.start();
                Iterator iterator = matchAttributeBoundaryList.iterator();
                while (iterator.hasNext()) {
                    AttributeBoundary ab = (AttributeBoundary) iterator.next();
                    if (ab.containsEquals(searchType, searchIndex)) {
                        highlighter.addHighlight(searchIndex, matcher.end(), highlightPainter);
                    }
                }
                if (firstIndex == -1 && searchIndex > -1) {
                    firstIndex = searchIndex;
                }
            }
            if (firstIndex > -1) {
                eventDetails.setCaretPosition(firstIndex);
            } else {
                StatusBar.getInstance().setDisplayText(0, InsightConstants.getLiteral("ERROR_FIND_FAILURE"), false);
            }
        } catch (BadLocationException ble) {
            ble.printStackTrace();
        }
        return ((firstIndex > -1));
    }
