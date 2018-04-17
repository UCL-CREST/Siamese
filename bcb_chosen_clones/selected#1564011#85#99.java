    private void highlight(String styleName, String regex) {
        Document doc = m_textPane.getDocument();
        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        try {
            CharSequence text = doc.getText(0, doc.getLength());
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                int start = matcher.start();
                int end = matcher.end();
                GuiUtil.setStyle(m_textPane, start, end - start, styleName);
            }
        } catch (BadLocationException e) {
            assert false;
        }
    }
