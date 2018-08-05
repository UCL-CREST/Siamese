    public static String getUrlFromFileTransferStanza(String stanza) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            Document d = factory.newDocumentBuilder().parse(new InputSource(new StringReader(stanza)));
            return d.getElementsByTagName("url").item(0).getTextContent();
        } catch (Exception e) {
            return null;
        }
    }
