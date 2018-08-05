    public void setColor(String regexp, ColorRGBA color) {
        Pattern p = Pattern.compile(regexp);
        Matcher m = p.matcher(block.getText());
        while (m.find()) {
            letters.setColor(m.start(), m.end(), color);
        }
        letters.invalidate();
        needRefresh = true;
    }
