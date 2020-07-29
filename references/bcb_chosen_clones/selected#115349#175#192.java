    private static void encode(ByteArrayOutputStream bytes, ServletOutputStream out) throws IOException {
        String regex = "(href|src|action) *= *\"([^\"]*)\"";
        SmartURLsFilter smartURLsFilter = new SmartURLsFilter();
        String replacement = smartURLsFilter.parameterSeparator + "$2" + smartURLsFilter.nameValueSeparator + "$4";
        smartURLsFilter.encodePattern = Pattern.compile("(" + "\\?|&" + ")([a-zA-Z0-9%+.-[*]_]*)" + "(" + "=" + ")([a-zA-Z0-9%+.-[*]_=/]*)");
        CharBuffer chars = CharBuffer.wrap(bytes.toString());
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(chars);
        int pos = 0;
        while (matcher.find()) {
            out.print(chars.subSequence(pos, matcher.start()).toString());
            pos = matcher.end();
            Matcher matcher2 = smartURLsFilter.encodePattern.matcher(matcher.group(2));
            String group2 = matcher2.replaceAll(replacement);
            out.print(matcher.group(1) + "=\"" + group2 + "\"");
        }
        out.print(chars.subSequence(pos, bytes.size()).toString());
    }
