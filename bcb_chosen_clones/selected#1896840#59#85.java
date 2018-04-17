    public void processChangedLines(int offset, int length) throws BadLocationException {
        String text = getText(0, getLength());
        highlightString(0, getLength(), defaultStyle);
        Set<String> keyw = keywords.keySet();
        for (String keyword : keyw) {
            Pattern p = Pattern.compile(keyword);
            Matcher m = p.matcher(text);
            while (m.find()) {
                highlightString(m.start(), m.end() - m.start(), keywords.get(keyword));
            }
        }
        Matcher mlcStart = multiCommentDelimStart.matcher(text);
        Matcher mlcEnd = multiCommentDelimEnd.matcher(text);
        while (mlcStart.find()) {
            if (mlcEnd.find(mlcStart.end())) {
                highlightString(mlcStart.start(), (mlcEnd.end() - mlcStart.start()), commentStyle);
            } else {
                highlightString(mlcStart.start(), getLength(), commentStyle);
            }
        }
        Matcher slc = singleCommentDelim.matcher(text);
        while (slc.find()) {
            int line = rootElement.getElementIndex(slc.start());
            int endOffset = rootElement.getElement(line).getEndOffset() - 1;
            highlightString(slc.start(), (endOffset - slc.start()), commentStyle);
        }
    }
