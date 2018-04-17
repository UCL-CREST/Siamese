    public static List<ResultObject> AnalyzeResult(String xmlString) throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource inStream = new org.xml.sax.InputSource();
        inStream.setCharacterStream(new java.io.StringReader(xmlString));
        Document doc = builder.parse(inStream);
        NodeList nodeList = doc.getElementsByTagName("result");
        List<ResultObject> list = new ArrayList<ResultObject>();
        for (int index = 0; index < nodeList.getLength(); index++) {
            String theTitle = null;
            String theURL = null;
            String theSummary = null;
            Node node = nodeList.item(index);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                NodeList nameNode = element.getElementsByTagName("title");
                if (nameNode.item(0).getNodeType() == Node.ELEMENT_NODE) {
                    Element nameElement = (Element) nameNode.item(0);
                    theTitle = nameElement.getFirstChild().getNodeValue().trim();
                }
                nameNode = element.getElementsByTagName("url");
                if (nameNode.item(0).getNodeType() == Node.ELEMENT_NODE) {
                    Element nameElement = (Element) nameNode.item(0);
                    theURL = nameElement.getFirstChild().getNodeValue().trim();
                }
                nameNode = element.getElementsByTagName("abstract");
                if (nameNode.item(0).getNodeType() == Node.ELEMENT_NODE) {
                    Element nameElement = (Element) nameNode.item(0);
                    if (nameElement.getFirstChild() == null) {
                        theSummary = "";
                    } else {
                        theSummary = nameElement.getFirstChild().getNodeValue();
                    }
                }
                ResultObject ro = new ResultObject(theTitle, theURL, theSummary);
                int no = index + 1;
                System.out.println("Result" + no);
                System.out.println(ro);
                String userFeedback = readUserInput("Relevant (Y/N)?\n");
                if (userFeedback.equals("y")) {
                    list.add(ro);
                }
            }
        }
        return list;
    }
