    public static void processStrings(StyledDocument doc, HighlightingStyleLoader styler, String word) {
        Style commentStyle = doc.addStyle("STRINGS", null);
        StyleConstants.setForeground(commentStyle, new Color(0, 255, 0));
        Pattern p1 = Pattern.compile("(\".*\")|(\'.*\')");
        Matcher m1 = p1.matcher(word);
        int start = 0;
        while (m1.find(start)) {
            System.out.println("MATCH start=" + m1.start() + " end=" + m1.end());
            doc.setCharacterAttributes(m1.start(), m1.end() - m1.start(), commentStyle, true);
            start = m1.end();
        }
    }
