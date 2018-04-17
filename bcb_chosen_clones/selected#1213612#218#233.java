    public static void parseTextInlinedElement(StringBuilder strb, String text, int start, int end) {
        Pattern boldPattern = Pattern.compile("(^|[\\. ,])([\\*\\+\\_])(.+)\\2($|[\\. ,])");
        Matcher matcher = boldPattern.matcher(text);
        matcher.region(start, end);
        int i = start, j, k;
        while (matcher.find()) {
            j = matcher.start(2);
            k = matcher.end(2);
            strb.append(text, i, j - 1);
            renderTextInlinedElement(strb, text, j, k);
            i = k + 1;
        }
        if (i < text.length()) {
            strb.append(text, i, end);
        }
    }
