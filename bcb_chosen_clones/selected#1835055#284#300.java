    private static void addText(String text, StyledDocument sd, Style style) {
        Pattern URL = Pattern.compile(StringHelper.URLRegex, Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
        Matcher matcher = URL.matcher(text);
        try {
            sd.insertString(sd.getLength(), text, style);
        } catch (BadLocationException e) {
        }
        Style urlStyle;
        while (matcher.find()) {
            urlStyle = sd.addStyle("link" + matcher.start(), null);
            urlStyle.addAttribute(IDENTIFIER_URL, matcher.group());
            StyleConstants.setForeground(urlStyle, Color.BLUE);
            StyleConstants.setBold(urlStyle, true);
            StyleConstants.setUnderline(urlStyle, true);
            sd.setCharacterAttributes(sd.getLength() - text.length() + matcher.start(), matcher.end() - matcher.start(), urlStyle, true);
        }
    }
