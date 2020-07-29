    protected void addTextNode(XmlViewNode target, String text) {
        Pattern pattern = Pattern.compile("\\#\\{[a-zA-Z_]+\\}", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(text);
        int beforeIndex = 0;
        while (matcher.find()) {
            int startIndex = matcher.start();
            if (beforeIndex < startIndex) {
                String subText = text.substring(beforeIndex, startIndex);
                XmlViewNode node = new XmlViewNode(subText);
                target.addNode(node);
            }
            String dollText = text.substring(startIndex, matcher.end());
            XmlViewNodeValue node = new XmlViewNodeValue();
            String valueName = dollText.substring(2, dollText.length() - 1);
            node.setValueName(valueName);
            target.addNode(node);
            beforeIndex = matcher.end();
        }
        if (beforeIndex < text.length()) {
            String lastText = text.substring(beforeIndex);
            XmlViewNode node = new XmlViewNode(lastText);
            target.addNode(node);
        }
    }
