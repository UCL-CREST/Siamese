    public static String getFromAttributeFromMessageStanza(String stanza) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            Document d = factory.newDocumentBuilder().parse(new InputSource(new StringReader(stanza)));
            return d.getAttributes().getNamedItem("from").toString();
        } catch (Exception e) {
            return null;
        }
    }
