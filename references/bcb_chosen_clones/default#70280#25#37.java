    public static void correct(int startID, String xmlFile) throws ParserConfigurationException, SAXException, IOException, TransformerConfigurationException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder buidler = factory.newDocumentBuilder();
        Document doc = buidler.parse(new File(xmlFile));
        NodeList childs = doc.getDocumentElement().getElementsByTagName("definition");
        for (int i = 0; i < childs.getLength(); i++) {
            Element element = (Element) childs.item(i);
            element.setAttribute("id", (startID + i) + "");
        }
        TransformerFactory tFact = TransformerFactory.newInstance();
        Transformer transform = tFact.newTransformer();
        transform.transform(new DOMSource(doc), new StreamResult(new File(xmlFile)));
    }
