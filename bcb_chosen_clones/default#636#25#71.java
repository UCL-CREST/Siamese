    public void lineGetStyle(LineStyleEvent e) {
        java.util.List styles = new java.util.ArrayList();
        Pattern pattern;
        Matcher matcher;
        if (e.lineText.trim().length() > 0) if (e.lineText.trim().charAt(0) != ';') {
            for (int i = 0; i < reservedWords.length; i++) {
                pattern = Pattern.compile("\\s" + reservedWords[i] + "(?![^;\\s]+)");
                matcher = pattern.matcher("\n" + e.lineText.toUpperCase().split(";")[0] + "\n");
                while (matcher.find()) {
                    styles.add(new StyleRange(e.lineOffset + matcher.start(), reservedWords[i].length(), new Color(display, 0, 0, 204), null, SWT.BOLD));
                }
            }
            for (int i = 0; i < registers.length; i++) {
                pattern = Pattern.compile("(\\s|,)" + registers[i] + "(?![^,;\\s]+)");
                matcher = pattern.matcher("\n" + e.lineText.toUpperCase().split(";")[0] + "\n");
                while (matcher.find()) {
                    styles.add(new StyleRange(e.lineOffset + matcher.start(), registers[i].length(), new Color(display, 102, 0, 51), null, SWT.BOLD));
                }
            }
            for (int i = 0; i < declarations.length; i++) {
                pattern = Pattern.compile("\\s" + declarations[i] + "(?![^;\\s]+)");
                matcher = pattern.matcher("\n" + e.lineText.toUpperCase().split(";")[0] + "\n");
                while (matcher.find()) {
                    styles.add(new StyleRange(e.lineOffset + matcher.start(), declarations[i].length(), new Color(this.display, 100, 100, 100), null, SWT.BOLD));
                }
            }
            pattern = Pattern.compile("^\\s\\w+:");
            matcher = pattern.matcher("\n" + e.lineText.toUpperCase().split(";")[0] + "\n");
            while (matcher.find()) {
                styles.add(new StyleRange(e.lineOffset + matcher.start() - 1, matcher.end() - (matcher.start()), null, null, SWT.BOLD));
            }
            pattern = Pattern.compile("(\\s|,)((0B([0-1]+))|(0X([0-9A-F]+))|([0-9]+))(?![^;\\s]+)");
            matcher = pattern.matcher("\n" + e.lineText.toUpperCase().split(";")[0] + "\n");
            while (matcher.find()) {
                styles.add(new StyleRange(e.lineOffset + matcher.start(), matcher.end() - matcher.start(), new Color(display, 240, 51, 0), null));
            }
            pattern = Pattern.compile("(\"[^\"]*\"|'[^']*')");
            matcher = pattern.matcher("\n" + e.lineText.toUpperCase().split(";")[0] + "\n");
            while (matcher.find()) {
                styles.add(new StyleRange(e.lineOffset + matcher.start() - 1, matcher.end() - (matcher.start()), new Color(display, 204, 0, 0), null));
            }
        }
        pattern = Pattern.compile("\\Q;\\E");
        matcher = pattern.matcher(e.lineText);
        if (matcher.find()) styles.add(new StyleRange(e.lineOffset + matcher.start(), e.lineText.length() - matcher.start(), new Color(display, 63, 127, 95), null, SWT.ITALIC));
        e.styles = (StyleRange[]) styles.toArray(new StyleRange[0]);
    }
