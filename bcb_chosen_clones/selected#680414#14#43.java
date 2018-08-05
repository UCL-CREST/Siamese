    public static List<Comment> splitComments(String s) {
        List<Comment> cl = new ArrayList<Comment>();
        if (s == null) return cl;
        String p = "<font\\scolor=[\"']#[0-9a-fA-F]{6}[\"']><b>([^,<>]+),\\s([0-9\\:\\-\\s\\/\\.]+)";
        Pattern pattern = Pattern.compile(p, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(s);
        String fullName = null;
        String created = null;
        String body = null;
        int prevEnd = -1;
        int begin = 0;
        int end = 0;
        while (matcher.find()) {
            begin = matcher.start();
            end = matcher.end();
            if (prevEnd >= 0) {
                body = s.substring(prevEnd, begin);
                addCommentIfNotEmpty(cl, fullName, created, body);
            }
            fullName = matcher.group(1).trim();
            created = matcher.group(2).trim();
            if (created.endsWith(":")) created = created.substring(0, created.length() - 1);
            prevEnd = end;
        }
        if (prevEnd >= 0) {
            body = s.substring(end, s.length());
            addCommentIfNotEmpty(cl, fullName, created, body);
        }
        return cl;
    }
