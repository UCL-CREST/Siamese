    @Override
    public List<SectionFinderResult> lookForSections(String text, Section<?> father, Type type) {
        ArrayList<SectionFinderResult> result = new ArrayList<SectionFinderResult>();
        Pattern TABLE_LINE = Pattern.compile(TABLE_LINE_REGEXP, Pattern.MULTILINE);
        Matcher m = TABLE_LINE.matcher(text);
        int end = 0;
        int tableStart = -1;
        int tableEnd = -1;
        while (m.find(end)) {
            int start = m.start();
            end = m.end();
            if (tableEnd == start) {
                tableEnd = end;
            } else {
                addResultIfAvailable(result, tableStart, tableEnd);
                tableStart = start;
                tableEnd = end;
            }
            if (end >= text.length()) break;
        }
        addResultIfAvailable(result, tableStart, tableEnd);
        return result;
    }
