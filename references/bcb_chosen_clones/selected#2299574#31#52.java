    public void lineGetStyle(LineStyleEvent e) {
        java.util.List styles = new java.util.ArrayList();
        Pattern pattern;
        Matcher matcher;
        if (e.lineText.trim().length() > 0) {
            if (e.lineText.trim().charAt(0) != ';') {
                for (int i = 0; i < core.getSyntaxParser().getStyleCount(); i++) {
                    if (i != core.getSyntaxParser().TYPE_COMMENT) {
                        pattern = Pattern.compile(core.getSyntaxParser().getRegularExpression(i), Pattern.CASE_INSENSITIVE);
                        matcher = pattern.matcher("\n" + e.lineText.toUpperCase().split(";")[0] + "\n");
                        while (matcher.find()) {
                            styles.add(new StyleRange(e.lineOffset + matcher.start() - 1, matcher.end() - matcher.start(), core.getSyntaxParser().getColor(i), null, core.getSyntaxParser().getStyle(i)));
                        }
                    }
                }
            }
            pattern = Pattern.compile(core.getSyntaxParser().getRegularExpression(core.getSyntaxParser().TYPE_COMMENT));
            matcher = pattern.matcher(e.lineText);
            if (matcher.find()) styles.add(new StyleRange(e.lineOffset + matcher.start(), e.lineText.length() - matcher.start(), core.getSyntaxParser().getColor(core.getSyntaxParser().TYPE_COMMENT), null, core.getSyntaxParser().getStyle(core.getSyntaxParser().TYPE_COMMENT)));
            e.styles = (StyleRange[]) styles.toArray(new StyleRange[0]);
        }
    }
