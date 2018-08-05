    private String replaceCCReferrerString(WikiContext context, String sourceText, String from, String to) {
        StringBuilder sb = new StringBuilder(sourceText.length() + 32);
        Pattern linkPattern = Pattern.compile("\\p{Lu}+\\p{Ll}+\\p{Lu}+[\\p{L}\\p{Digit}]*");
        Matcher matcher = linkPattern.matcher(sourceText);
        int start = 0;
        while (matcher.find(start)) {
            String match = matcher.group();
            sb.append(sourceText.substring(start, matcher.start()));
            int lastOpenBrace = sourceText.lastIndexOf('[', matcher.start());
            int lastCloseBrace = sourceText.lastIndexOf(']', matcher.start());
            if (match.equals(from) && lastCloseBrace >= lastOpenBrace) {
                sb.append(to);
            } else {
                sb.append(match);
            }
            start = matcher.end();
        }
        sb.append(sourceText.substring(start));
        return sb.toString();
    }
