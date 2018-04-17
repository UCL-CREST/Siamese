    Vector match(int searchType, String searchExpression, TimedSegment ts, String transcriptionName, String transcriptionPath, String tierID, String speakerID, String speakerAbb, String[][] metaData) {
        Vector result = new Vector();
        switch(searchType) {
            case AbstractSearch.STRING_SEARCH_TYPE:
                String desc = ts.getDescription();
                int fromIndex = 0;
                while (fromIndex < desc.length()) {
                    int matchPosition = desc.indexOf(searchExpression, fromIndex);
                    if (matchPosition >= 0) {
                        TranscriptionSearchResultItem item = new TranscriptionSearchResultItem(transcriptionName, transcriptionPath, tierID, speakerID, speakerAbb, ts, matchPosition, searchExpression.length(), metaData);
                        result.addElement(item);
                        fromIndex = matchPosition + 1;
                    } else {
                        break;
                    }
                }
                break;
            case AbstractSearch.REGULAR_EXPRESSION_SEARCH_TYPE:
                Pattern pattern = Pattern.compile(searchExpression);
                Matcher matcher = pattern.matcher(ts.getDescription());
                matcher.reset();
                while (matcher.find()) {
                    int start = matcher.start();
                    int end = matcher.end();
                    TranscriptionSearchResultItem item = new TranscriptionSearchResultItem(transcriptionName, transcriptionPath, tierID, speakerID, speakerAbb, ts, start, end - start, metaData);
                    result.addElement(item);
                }
                break;
            case AbstractSearch.XPATH_EXPRESSION_SEARCH_TYPE:
                break;
            default:
        }
        return result;
    }
