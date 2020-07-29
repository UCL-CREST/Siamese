    private int refreshHighlighters() throws BadLocationException {
        int counter = 0;
        try {
            final String regExp = textField.getText();
            Highlighter hilite = textArea.getHighlighter();
            final Document doc = textArea.getDocument();
            final String text = doc.getText(0, doc.getLength());
            final Pattern pattern = Pattern.compile(regExp);
            final Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                for (int i = 1; i <= matcher.groupCount(); i++) {
                    hilite.addHighlight(matcher.start(i), matcher.end(i), myHighlightPainterDarker);
                }
                hilite.addHighlight(matcher.start(), matcher.end(), myHighlightPainter);
                counter++;
            }
            label.setText("");
        } catch (BadLocationException e) {
            showExceptionMessage(e);
        } catch (PatternSyntaxException e) {
            showExceptionMessage(e);
        } catch (IllegalStateException e) {
            showExceptionMessage(e);
        } catch (RuntimeException e) {
            showExceptionMessage(e);
        }
        return counter;
    }
