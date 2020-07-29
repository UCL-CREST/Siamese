    public CJobProposition() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        XPath xpath = XPathFactory.newInstance().newXPath();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse("PropositionEx.xml");
            Element element = doc.getDocumentElement();
            NodeList ndlst = element.getChildNodes();
            ndlst = (NodeList) xpath.evaluate("/proposition/*", element, XPathConstants.NODESET);
            for (int i = 0; i < ndlst.getLength(); i++) {
                Node nd = ndlst.item(i);
                System.out.println(i);
                System.out.println(nd.getNodeName());
                System.out.println(nd.getTextContent());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
