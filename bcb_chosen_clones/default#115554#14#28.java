    public static void main(String[] args) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        XPath xpath = XPathFactory.newInstance().newXPath();
        Document doc;
        CField field;
        try {
            JobObjectMaker.newJobObject("proposition");
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse("PropositionStruct.xml");
            Element element = doc.getDocumentElement();
            NodeList fieldNds = (NodeList) xpath.evaluate("//field", element, XPathConstants.NODESET);
            for (int i = 0; i < fieldNds.getLength(); i++) field = new CField((Element) fieldNds.item(i));
        } catch (Exception e) {
        }
    }
