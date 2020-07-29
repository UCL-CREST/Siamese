    public static void exampleAsSerializer(String sourceID, String xslID) throws TransformerException, TransformerConfigurationException, SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dfactory.newDocumentBuilder();
        org.w3c.dom.Document outNode = docBuilder.newDocument();
        Node doc = docBuilder.parse(new InputSource(sourceID));
        TransformerFactory tfactory = TransformerFactory.newInstance();
        Transformer serializer = tfactory.newTransformer();
        Properties oprops = new Properties();
        oprops.put("method", "html");
        oprops.put("{http://icl.com/saxon}indent-spaces", "2");
        serializer.setOutputProperties(oprops);
        serializer.transform(new DOMSource(doc), new StreamResult(System.out));
    }
