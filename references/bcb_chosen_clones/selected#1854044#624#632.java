    public static void makeSpanFromPattern(SpannableStringBuilder ssb, String pattern, GroupListener lsr) {
        if (pattern == null || pattern.length() == 0 || lsr == null) return;
        Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(ssb);
        while (m.find()) {
            String g = m.group();
            lsr.onPatternMatch(g, ssb, m.start(), m.end());
        }
    }
