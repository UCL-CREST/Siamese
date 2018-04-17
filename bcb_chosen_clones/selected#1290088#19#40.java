    public static String stripTags(String text, String tags) {
        tags = " " + tags + " ";
        final Pattern p = Pattern.compile("</?(.*?)(s.*?)?>");
        final Matcher m = p.matcher(text);
        final StringBuffer out = new StringBuffer();
        int lastPos = 0;
        while (m.find()) {
            final String tag = m.group(1);
            if (tags.indexOf(tag) == -1) {
                out.append(text.substring(lastPos, m.start())).append(" ");
            } else {
                out.append(text.substring(lastPos, m.end()));
            }
            lastPos = m.end();
        }
        if (lastPos > 0) {
            out.append(text.substring(lastPos));
            return out.toString().trim();
        } else {
            return text;
        }
    }
