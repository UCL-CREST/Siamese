    public static String getMessageBodyFromMessageStanza(String stanza) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            Document d = factory.newDocumentBuilder().parse(new InputSource(new StringReader(stanza)));
            Node n = d.getElementsByTagName("body").item(0);
            return n.getTextContent();
        } catch (Exception e) {
            return null;
        }
    }
