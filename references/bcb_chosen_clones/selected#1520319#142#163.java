    public void setStyleRange(Map<String, StyleRange> styles) {
        List<StyleRange> finalStyles = new ArrayList<StyleRange>();
        for (String str : styles.keySet()) {
            StyleRange styleRange = styles.get(str);
            if (styleRange.length == 0) {
                Pattern patternVariable = Pattern.compile(str);
                Matcher matcher = patternVariable.matcher(messageDetail);
                while (matcher.find()) {
                    int start = matcher.start();
                    int end = matcher.end();
                    int length = end - start;
                    StyleRange newStyleRange = new StyleRange(start, length, styleRange.foreground, styleRange.background, styleRange.fontStyle);
                    finalStyles.add(newStyleRange);
                }
            } else {
                finalStyles.add(styleRange);
            }
        }
        for (StyleRange styleRange : finalStyles) {
            styledText.setStyleRange(styleRange);
        }
    }
