    public static void replacePattern(SpannableStringBuilder ssb, String source, String pattern, PatternListener lsr) {
        if (pattern == null || pattern.length() == 0 || lsr == null) return;
        Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(source);
        int s = 0;
        while (m.find()) {
            ssb.append(source.subSequence(s, m.start()));
            lsr.onPatternMatch(m.group(), ssb);
            s = m.end();
        }
        ssb.append(source.substring(s));
        lsr.onEnd(ssb);
    }
