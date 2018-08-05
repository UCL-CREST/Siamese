    public static String getToAttributeFromMessageStanza(String stanza) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            Document d = factory.newDocumentBuilder().parse(new InputSource(new StringReader(stanza)));
            return d.getAttributes().getNamedItem("to").toString();
        } catch (Exception e) {
            return null;
        }
    }
