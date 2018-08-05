    public static String autoLink(final String txt) {
        if (url_pattern == null) {
            url_pattern = Pattern.compile("(http(s)?|ftp)://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?");
        }
        final StringBuilder html = new StringBuilder();
        int lastIdx = 0;
        final Matcher matchr = url_pattern.matcher(txt);
        while (matchr.find()) {
            final String str = matchr.group();
            html.append(txt.substring(lastIdx, matchr.start()));
            html.append("<a target=\"_blank\" href=\"");
            html.append(str).append("\">").append(str).append("</a>");
            lastIdx = matchr.end();
        }
        html.append(txt.substring(lastIdx));
        return html.toString();
    }
