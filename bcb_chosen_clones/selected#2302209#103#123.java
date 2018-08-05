    private void matcher(String pattern, int os, int len) {
        String data = null;
        try {
            data = target.getDocument().getText(os, len);
            Pattern p = null;
            if (isIgnoreCase()) p = Pattern.compile(pattern, Pattern.MULTILINE | Pattern.CASE_INSENSITIVE); else p = Pattern.compile(pattern, Pattern.MULTILINE);
            Matcher m = p.matcher(data);
            if (m.find()) {
                do {
                    target.setCaretPosition(os + m.start());
                    start = os + m.start();
                    end = os + m.end();
                    found = true;
                    if (NEXT.equals(direction)) break;
                } while (m.find());
            } else {
                resetEngine();
            }
        } catch (BadLocationException ex) {
        }
    }
