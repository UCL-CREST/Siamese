    public void processChangedLines(int offset, int length) throws BadLocationException {
        String text = getText(0, getLength());
        highlightString(Color.black, 0, getLength(), true, false);
        Set<String> keyw = keywords.keySet();
        for (String keyword : keyw) {
            Color col = keywords.get(keyword);
            Pattern p = Pattern.compile("\\b" + keyword + "\\b");
            Matcher m = p.matcher(text);
            while (m.find()) {
                highlightString(col, m.start(), keyword.length(), true, true);
            }
        }
        Matcher mlcStart = multiLineCommentDelimiterStart.matcher(text);
        Matcher mlcEnd = multiLineCommentDelimiterEnd.matcher(text);
        while (mlcStart.find()) {
            if (mlcEnd.find(mlcStart.end())) {
                highlightString(commentColor, mlcStart.start(), (mlcEnd.end() - mlcStart.start()), true, true);
            } else {
                highlightString(commentColor, mlcStart.start(), getLength(), true, true);
            }
        }
        Matcher slc = singleLineCommentDelimiter.matcher(text);
        while (slc.find()) {
            int line = rootElement.getElementIndex(slc.start());
            int endOffset = rootElement.getElement(line).getEndOffset() - 1;
            highlightString(commentColor, slc.start(), (endOffset - slc.start()), true, true);
        }
        int initial = -1;
        Matcher stringMatcher = stringPattern.matcher(text);
        while (stringMatcher.find()) {
            if (initial == -1) {
                initial = stringMatcher.start();
            } else {
                highlightString(stringColor, initial, ((stringMatcher.start() + (stringMatcher.end() - stringMatcher.start())) - initial), true, true);
                initial = -1;
            }
        }
        if (initial >= 0) {
            highlightString(stringColor, initial, getLength(), true, true);
            initial = -1;
        }
        Matcher variableMatcher = variablePattern.matcher(text);
        while (variableMatcher.find()) {
            highlightString(variableColor, variableMatcher.start(), variableMatcher.end() - variableMatcher.start(), true, true);
        }
        Matcher attributeMatcher = attributePattern.matcher(text);
        while (attributeMatcher.find()) {
            highlightString(attributeColor, attributeMatcher.start(), attributeMatcher.end() - attributeMatcher.start(), true, true);
        }
        Matcher numberMatcher = numberPattern.matcher(text);
        while (numberMatcher.find()) {
            highlightString(Color.BLUE, numberMatcher.start(), numberMatcher.end() - numberMatcher.start(), true, true);
        }
    }
