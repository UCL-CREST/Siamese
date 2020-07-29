    private void findHighlights() {
        Pattern p = Pattern.compile(getRegex(), getRegexOptions());
        Matcher m = p.matcher("");
        List<String> lines = getTextInLines();
        for (int i = 0; i < lines.size(); i++) {
            m.reset(lines.get(i));
            while (m.find()) {
                int groupCount = m.groupCount();
                for (int j = 0; j <= groupCount; j++) {
                    highlightMatched(m.start(j), m.end(j), i, j);
                }
            }
        }
    }
