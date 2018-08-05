    public static String remap(String before, String after, String sql) {
        if (LOG.isTraceEnabled()) LOG.trace("Remapping before: " + before + ", after: " + after + ", sql: " + sql);
        StringBuilder out = new StringBuilder(sql.length() + 128);
        Pattern pattern = Pattern.compile("(^|\\W+)(" + before + ")(\\W+|$)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(sql);
        int lastIndex = 0;
        while (matcher.find()) {
            if (LOG.isTraceEnabled()) LOG.trace("Found match at index " + matcher.start(2));
            out.append(sql.substring(lastIndex, matcher.start(2)));
            out.append(after);
            lastIndex = matcher.end(2);
        }
        out.append(sql.substring(lastIndex));
        if (LOG.isTraceEnabled()) LOG.trace("Result: " + out);
        return out.toString();
    }
