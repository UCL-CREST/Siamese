    private static String convertURLs(String string) {
        StringBuffer sb = new StringBuffer();
        int lastEnd = 0;
        Pattern pattern = Pattern.compile("\\[[^\\[\\]]+\\]|[\\S]+");
        Matcher matcher = pattern.matcher(string);
        while (matcher.find()) {
            String skipped = string.substring(lastEnd, matcher.start());
            sb.append(skipped);
            String segment = string.substring(matcher.start(), matcher.end());
            if (segment.startsWith("[")) {
                sb.append(processBracketed(segment));
            } else {
                sb.append(processNotBracketed(segment));
            }
            lastEnd = matcher.end();
        }
        String skipped = string.substring(lastEnd, string.length());
        sb.append(skipped);
        return sb.toString();
    }
