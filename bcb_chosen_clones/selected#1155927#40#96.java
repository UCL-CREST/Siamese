    protected static boolean search(JTextComponent text, String search, boolean matchCase, boolean forward) {
        Pattern pattern = null;
        String regularSearch = "\\Q" + prepareNonRegularExpression(search) + "\\E";
        int start = -1;
        int end = -1;
        if (!matchCase) {
            pattern = Pattern.compile(regularSearch, Pattern.CASE_INSENSITIVE);
        } else {
            pattern = Pattern.compile(regularSearch);
        }
        try {
            int caret = text.getCaretPosition();
            if (getHiglight(text) != null) {
                caret = Math.max(caret, Math.max(getHighlightStart(text), getHighlightEnd(text)));
            }
            Matcher matcher = pattern.matcher(text.getText(0, text.getDocument().getLength()));
            if (forward) {
                boolean match = matcher.find(caret);
                if (!match) {
                    match = matcher.find(0);
                }
                if (match) {
                    start = matcher.start();
                    end = matcher.end();
                }
            } else {
                caret = text.getCaretPosition();
                if (getHiglight(text) != null) {
                    caret = Math.min(caret, Math.min(getHighlightStart(text), getHighlightEnd(text)));
                }
                while (matcher.find()) {
                    if (matcher.start() < caret) {
                        start = matcher.start();
                        end = matcher.end();
                    } else {
                        break;
                    }
                }
                if (start == -1) {
                    boolean match = matcher.find(caret);
                    while (match) {
                        start = matcher.start();
                        end = matcher.end();
                        match = matcher.find();
                    }
                }
            }
            if (end != -1) {
                matcher.find(start);
                select(text, start, end);
            }
            text.repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return end != -1;
    }
