    private static String formatBookmark(String input) {
        Pattern p;
        Matcher m;
        StringBuilder buffer = new StringBuilder(1024);
        input = input.replaceAll("\\s+", " ");
        p = Pattern.compile("\\\\[\"\']{1}");
        m = p.matcher(input);
        int offset = 0;
        buffer.append(input);
        while (m.find()) {
            buffer.delete(m.start() - offset, m.end() - offset);
            offset += (m.end() - m.start());
        }
        input = buffer.toString();
        input = input.replace('\'', '"');
        return input.replaceAll("\\s*=\\s*", "=");
    }
