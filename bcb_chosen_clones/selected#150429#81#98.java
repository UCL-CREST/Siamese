    private static void processComments(StyledDocument doc, HighlightingStyleLoader styler, String word) {
        Style commentStyle = doc.addStyle("COMMENT", null);
        StyleConstants.setForeground(commentStyle, new Color(0, 255, 0));
        Pattern p1 = Pattern.compile("(([/]{2}.*[^\"])\\n)|(([/]{2}.*[^\"])$)");
        Matcher m1 = p1.matcher(word);
        int start = 0;
        while (m1.find(start)) {
            doc.setCharacterAttributes(m1.start(), m1.end() - m1.start(), commentStyle, true);
            start = m1.end();
        }
        Pattern p2 = Pattern.compile("/\\*.*\\*/");
        Matcher m2 = p2.matcher(word);
        start = 0;
        while (m2.find(start)) {
            doc.setCharacterAttributes(m2.start(), m2.end() - m2.start(), commentStyle, true);
            start = m2.end();
        }
    }
