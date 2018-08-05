    private static String unEscape(String s) throws java.text.ParseException {
        final String qr = java.util.regex.Matcher.quoteReplacement("\\");
        final java.util.regex.Pattern p = java.util.regex.Pattern.compile("" + "([^\\\\]\\\\[1-3][0-7][0-7]" + "|[^\\\\]\\\\[1-7][0-7]" + "|[^\\\\]\\\\[0-7]" + ")");
        s = s.replaceAll("\\\\=", "=");
        s = s.replaceAll("\\\\ ", " ");
        s = s.replaceAll("\\\\000", " ");
        s = s.replaceAll("\\\\\t", "\t");
        final java.util.regex.Matcher m = p.matcher(s);
        while (m.find()) {
            int a = m.start() + 1;
            int b = m.end() - 1;
            throw new java.text.ParseException("unEscape: octal is currently" + " unsupported (pos=" + a + ":" + b + ")", 0);
        }
        s = s.replaceAll("\\\\\\\\", qr);
        return s;
    }
