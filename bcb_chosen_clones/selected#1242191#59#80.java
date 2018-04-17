    public String foreach(String s, String regexp, String replacement) {
        Pattern rp = Pattern.compile("\\$(\\d)");
        Matcher rm = rp.matcher(replacement);
        StringBuffer res = new StringBuffer();
        Pattern p = Pattern.compile(regexp);
        Matcher m = p.matcher(s);
        int groupCount = m.groupCount();
        int end = 0;
        while (m.find()) {
            while (rm.find(end)) {
                res.append(replacement.substring(end, rm.start()));
                int g = Integer.parseInt(rm.group(1));
                if (0 <= g && g <= groupCount) {
                    res.append(m.group(g));
                }
                end = rm.end();
            }
            res.append(replacement.substring(end));
            end = 0;
        }
        return res.toString();
    }
