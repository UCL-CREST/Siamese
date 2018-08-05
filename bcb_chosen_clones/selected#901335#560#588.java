    public static int replaceAll(JTextArea textArea, String toFind, String replaceWith, boolean matchCase, boolean wholeWord, boolean regex) throws PatternSyntaxException {
        int count = 0;
        if (regex) {
            StringBuffer sb = new StringBuffer();
            String findIn = textArea.getText();
            int lastEnd = 0;
            Pattern p = Pattern.compile(toFind);
            Matcher m = p.matcher(findIn);
            try {
                while (m.find()) {
                    sb.append(findIn.substring(lastEnd, m.start()));
                    sb.append(getReplacementText(m, replaceWith));
                    lastEnd = m.end();
                    count++;
                }
                sb.append(findIn.substring(lastEnd));
                textArea.setText(sb.toString());
            } finally {
                findIn = null;
            }
        } else {
            textArea.setCaretPosition(0);
            while (SearchEngine.find(textArea, toFind, true, matchCase, wholeWord, false)) {
                textArea.replaceSelection(replaceWith);
                count++;
            }
        }
        return count;
    }
