    @SuppressWarnings("unused")
    private void search() {
        JTextArea jTextArea1 = this.text;
        removeHighlights(jTextArea1);
        String regExp = jTextArea1.getSelectedText();
        if (regExp == null || regExp.length() == 0) {
        }
        if (regExp != null && regExp.length() > 0) {
            Pattern pattern = Pattern.compile(regExp);
            String text = jTextArea1.getText();
            Matcher m = pattern.matcher(text);
            Highlighter hlter = jTextArea1.getHighlighter();
            while (m.find()) {
                int start = m.start();
                int end = m.end();
                try {
                    hlter.addHighlight(start, end, myHighlightPainter);
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            }
        }
    }
