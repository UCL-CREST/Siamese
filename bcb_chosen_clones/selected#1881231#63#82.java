    Vector match(int searchType, String searchExpression, AtomicTimedSegment ats, String transcriptionName, String transcriptionPath, String tierID, String speakerID, String speakerAbb, String category) {
        Vector result = new Vector();
        switch(searchType) {
            case AbstractSearch.REGULAR_EXPRESSION_SEARCH_TYPE:
                Pattern pattern = Pattern.compile(searchExpression);
                Matcher matcher = pattern.matcher(ats.getDescription());
                matcher.reset();
                while (matcher.find()) {
                    int start = matcher.start();
                    int end = matcher.end();
                    DescriptionSearchResultItem item = new DescriptionSearchResultItem(transcriptionName, transcriptionPath, tierID, speakerID, speakerAbb, ats, start, end - start, category);
                    result.addElement(item);
                }
                break;
            case AbstractSearch.XPATH_EXPRESSION_SEARCH_TYPE:
                break;
            default:
        }
        return result;
    }
