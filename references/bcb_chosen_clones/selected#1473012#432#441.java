    private void applyStyle(String keyword, Style style, String containingLine, int lineStartOffset) {
        String quoted = Pattern.quote(keyword);
        Pattern p = Pattern.compile("(^|" + Parser.DELIM + ")(" + quoted + ")(" + Parser.DELIM + "|$)");
        int matchstart = 0;
        Matcher m = p.matcher(containingLine.toUpperCase());
        while (m.find(matchstart)) {
            setCharacterAttributes(lineStartOffset + m.start(2), keyword.length(), style, true);
            matchstart = m.end(2);
        }
    }
