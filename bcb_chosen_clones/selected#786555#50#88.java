    public void run() {
        StyledDocument doc = mEdtPane.getStyledDocument();
        mEdtPane.removeCaretListener(mEdtPane);
        String text = null;
        try {
            text = doc.getText(mOffset, mLength);
        } catch (BadLocationException e) {
            e.printStackTrace();
            System.exit(1);
        }
        StyleContext sc = StyleContext.getDefaultStyleContext();
        Pattern p1 = Pattern.compile(".");
        Matcher m = p1.matcher(text);
        while (m.find()) {
            doc.setCharacterAttributes(mOffset + m.start(), m.end() - m.start(), (AttributeSet) sc.getStyle(SyntaxHighlighter.REGULAR_STYLE), true);
        }
        Pattern p = Pattern.compile("begin|end");
        m = p.matcher(text);
        while (m.find()) {
            doc.setCharacterAttributes(mOffset + m.start(), m.end() - m.start(), (AttributeSet) sc.getStyle(SyntaxHighlighter.KEYWORD_SYTLE), true);
        }
        Pattern strp = Pattern.compile("\".*\"");
        m = strp.matcher(text);
        while (m.find()) {
            doc.setCharacterAttributes(mOffset + m.start(), m.end() - m.start(), (AttributeSet) sc.getStyle(SyntaxHighlighter.STRING_STYLE), true);
        }
        Pattern sp = Pattern.compile("--.*");
        m = sp.matcher(text);
        while (m.find()) {
            doc.setCharacterAttributes(mOffset + m.start(), m.end() - m.start(), (AttributeSet) sc.getStyle(SyntaxHighlighter.SINGLE_COMMENT_STYLE), true);
        }
        Pattern mp = Pattern.compile("/\\*(.|[\n\r])*\\*/\\s*");
        m = mp.matcher(text);
        while (m.find()) {
            System.err.println("MATCHED");
            doc.setCharacterAttributes(mOffset + m.start(), m.end() - m.start(), (AttributeSet) sc.getStyle(SyntaxHighlighter.MULTIPLE_COMMENT_STYLE), true);
        }
        mEdtPane.addCaretListener(mEdtPane);
    }
