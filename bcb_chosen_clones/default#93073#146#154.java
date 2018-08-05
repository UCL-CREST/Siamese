    static Document loadDocument(File input) throws IOException, SAXException {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            return dbf.newDocumentBuilder().parse(input);
        } catch (ParserConfigurationException e) {
            throw new Error(e);
        }
    }
