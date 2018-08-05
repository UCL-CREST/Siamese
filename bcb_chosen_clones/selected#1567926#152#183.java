    private String replaceReferrerString(WikiContext context, String sourceText, String from, String to) {
        StringBuilder sb = new StringBuilder(sourceText.length() + 32);
        Pattern linkPattern = Pattern.compile("([\\[\\~]?)\\[([^\\|\\]]*)(\\|)?([^\\|\\]]*)(\\|)?([^\\|\\]]*)\\]");
        Matcher matcher = linkPattern.matcher(sourceText);
        int start = 0;
        while (matcher.find(start)) {
            char charBefore = (char) -1;
            if (matcher.start() > 0) charBefore = sourceText.charAt(matcher.start() - 1);
            if (matcher.group(1).length() > 0 || charBefore == '~' || charBefore == '[') {
                sb.append(sourceText.substring(start, matcher.end()));
                start = matcher.end();
                continue;
            }
            String text = matcher.group(2);
            String link = matcher.group(4);
            String attr = matcher.group(6);
            if (link.length() == 0) {
                text = replaceSingleLink(context, text, from, to);
            } else {
                link = replaceSingleLink(context, link, from, to);
                text = TextUtil.replaceString(text, from, to);
            }
            sb.append(sourceText.substring(start, matcher.start()));
            sb.append("[" + text);
            if (link.length() > 0) sb.append("|" + link);
            if (attr.length() > 0) sb.append("|" + attr);
            sb.append("]");
            start = matcher.end();
        }
        sb.append(sourceText.substring(start));
        return sb.toString();
    }
