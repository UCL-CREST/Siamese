    public void processChangedLines(int offset, int length) throws BadLocationException {
        String text = getText(0, getLength());
        highlightString(Color.black, 0, getLength(), true, false, false);
        Set<String> keyw = keywords.keySet();
        for (String keyword : keyw) {
            Color col = keywords.get(keyword);
            Pattern p = Pattern.compile("\\b" + keyword + "\\b");
            Matcher m = p.matcher(text);
            while (m.find()) {
                highlightString(col, m.start(), keyword.length(), true, true, false);
            }
        }
        int mlsStart = text.indexOf(stringDelimiter);
        while (mlsStart > -1) {
            int mlsEnd = text.indexOf(stringDelimiter, mlsStart + 1);
            mlsEnd = (mlsEnd < 0 ? text.length() - 1 : mlsEnd);
            highlightString(stringColor, mlsStart, (mlsEnd - (mlsStart - 1)), true, false, false);
            mlsStart = text.indexOf(stringDelimiter, mlsEnd + 1);
        }
        int mlqStart = text.indexOf(quoteDelimiter);
        while (mlqStart > -1) {
            int mlqEnd = text.indexOf(quoteDelimiter, mlqStart + 1);
            mlqEnd = (mlqEnd < 0 ? text.length() - 1 : mlqEnd);
            highlightString(quoteColor, mlqStart, (mlqEnd - (mlqStart - 1)), true, false, false);
            mlqStart = text.indexOf(quoteDelimiter, mlqEnd + 1);
        }
        Matcher mlcStart = multiLineCommentDelimiterStart.matcher(text);
        Matcher mlcEnd = multiLineCommentDelimiterEnd.matcher(text);
        while (mlcStart.find()) {
            if (mlcEnd.find(mlcStart.end())) highlightString(commentColor, mlcStart.start(), (mlcEnd.end() - mlcStart.start()), true, false, true); else highlightString(commentColor, mlcStart.start(), getLength(), true, false, true);
        }
        Matcher slc = singleLineCommentDelimter.matcher(text);
        while (slc.find()) {
            int line = rootElement.getElementIndex(slc.start());
            int endOffset = rootElement.getElement(line).getEndOffset() - 1;
            highlightString(commentColor, slc.start(), (endOffset - slc.start()), true, false, true);
        }
    }
