    public static void processTokens(StyledDocument doc, HighlightingStyleLoader styler, String word) {
        String delim = "[\\W]";
        Pattern p = Pattern.compile(delim);
        Matcher m = p.matcher(word);
        int start = 0;
        while (m.find(start)) {
            start = m.start();
            int end = m.end() - start;
            if (m.group().trim().length() > 0) {
                Style applyStyle = styler.getStyle(m.group());
                if (applyStyle != null) {
                    doc.setCharacterAttributes(start, end, applyStyle, true);
                }
            }
            start = m.end();
        }
        String[] t = word.split(delim);
        int startpt = 0;
        for (String tkn : t) {
            if (tkn.length() < 1) continue;
            startpt = word.indexOf(tkn, startpt);
            int endpt_orig = startpt + tkn.length();
            int endpt = tkn.length();
            Style applyStyle = styler.getStyle(word.substring(startpt, endpt_orig));
            if (applyStyle != null) {
                doc.setCharacterAttributes(startpt, endpt, applyStyle, true);
            } else {
                doc.setCharacterAttributes(startpt, endpt, defaultStyle, true);
            }
            startpt = endpt_orig;
        }
    }
