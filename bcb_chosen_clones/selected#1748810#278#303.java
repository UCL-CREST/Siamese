    private void highlightSyntax(String text) {
        IGrammar grammar = fGrammar;
        if (grammar == null) return;
        StyledText styledText = getTextWidget();
        IModelElement[] literals = ModelElementQuery.collectLiterals(grammar);
        if (literals != null && literals.length > 0) {
            Pattern pattern = Pattern.compile("(\\w(\\w|\\d)*)|(\\S)");
            Matcher matcher = pattern.matcher(text);
            List<StyleRange> styleRanges = new ArrayList<StyleRange>();
            while (matcher.find()) {
                String word = matcher.group();
                if (word.length() > 1) {
                    for (IModelElement literal : literals) {
                        String elementName = literal.getElementName().substring(1, literal.getElementName().length() - 1);
                        if (elementName.equals(word)) {
                            int start = matcher.start();
                            int length = matcher.end() - matcher.start();
                            styleRanges.add(createStyleRange(start, length));
                            break;
                        }
                    }
                }
            }
            styledText.setStyleRanges(styleRanges.toArray(new StyleRange[styleRanges.size()]));
        }
    }
