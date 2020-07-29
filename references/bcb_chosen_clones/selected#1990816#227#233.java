    public void setStyle(String regexp, int style) {
        Pattern p = Pattern.compile(regexp);
        Matcher m = p.matcher(block.getText());
        while (m.find()) {
            setStyle(m.start(), m.end(), style);
        }
    }
