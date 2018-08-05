    public void colourText(int offset, int length) {
        try {
            Element root = this.getDefaultRootElement();
            int startElementIndex = root.getElementIndex(offset);
            int endElementIndex = root.getElementIndex(offset + length);
            int startOff = root.getElement(startElementIndex).getStartOffset();
            int endOff = root.getElement(endElementIndex).getEndOffset();
            String text = getText(startOff, endOff - startOff);
            this.setCharacterAttributes(startOff, endOff - startOff, new SimpleAttributeSet(), true);
            Pattern tagReg = Pattern.compile("<(.|\\n)*?>");
            Matcher matches = tagReg.matcher(text);
            while (matches.find()) {
                this.setCharacterAttributes(startOff + matches.start(), matches.end() - matches.start(), tagStyle, true);
            }
        } catch (Exception e) {
        }
    }
