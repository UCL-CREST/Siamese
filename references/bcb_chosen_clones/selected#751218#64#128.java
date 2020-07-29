    public boolean fit(int index, double width, boolean unspacedFitting) {
        beginIndex = index;
        this.width = width;
        fittedText = null;
        fittedWidth = 0;
        String hyphen = "";
        fitting: {
            Pattern pattern = Pattern.compile("(\\s*)(\\S*)");
            Matcher matcher = pattern.matcher(text);
            matcher.region(beginIndex, text.length());
            while (matcher.find()) {
                for (int spaceIndex = matcher.start(1), spaceEnd = matcher.end(1); spaceIndex < spaceEnd; spaceIndex++) {
                    switch(text.charAt(spaceIndex)) {
                        case '\n':
                        case '\r':
                            index = spaceIndex;
                            break fitting;
                    }
                }
                int wordEndIndex = matcher.end(0);
                double wordWidth = font.getWidth(matcher.group(0), fontSize);
                fittedWidth += wordWidth;
                if (fittedWidth > width) {
                    fittedWidth -= wordWidth;
                    wordEndIndex = index;
                    if (!hyphenation && (wordEndIndex > beginIndex || !unspacedFitting || text.charAt(beginIndex) == ' ')) break fitting;
                    hyphenating: while (true) {
                        char textChar = text.charAt(wordEndIndex);
                        wordWidth = font.getWidth(textChar, fontSize);
                        wordEndIndex++;
                        fittedWidth += wordWidth;
                        if (fittedWidth > width) {
                            fittedWidth -= wordWidth;
                            wordEndIndex--;
                            if (hyphenation) {
                                if (wordEndIndex > index + 4) {
                                    wordEndIndex--;
                                    index = wordEndIndex;
                                    textChar = text.charAt(wordEndIndex);
                                    fittedWidth -= font.getWidth(textChar, fontSize);
                                    textChar = hyphenationCharacter;
                                    fittedWidth += font.getWidth(textChar, fontSize);
                                    hyphen = String.valueOf(textChar);
                                } else {
                                    while (wordEndIndex > index) {
                                        wordEndIndex--;
                                        textChar = text.charAt(wordEndIndex);
                                        fittedWidth -= font.getWidth(textChar, fontSize);
                                    }
                                }
                            } else {
                                index = wordEndIndex;
                            }
                            break hyphenating;
                        }
                    }
                    break fitting;
                }
                index = wordEndIndex;
            }
        }
        fittedText = text.substring(beginIndex, index) + hyphen;
        endIndex = index;
        return (fittedWidth > 0);
    }
