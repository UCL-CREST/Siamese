    public static void extractPropertiesFromTB3Ontology(File TB3OntologyXML, File OutputXML) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException, TransformerFactoryConfigurationError, TransformerException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(OutputXML));
        writer.write("<extractedTB3ontologyProperties>\n");
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        docFactory.setNamespaceAware(true);
        Document TB3XMLdoc = docBuilder.parse(TB3OntologyXML);
        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        String[] xPaths = new String[] { "//TextProperties", "//ImageProperties", "//Thing" };
        int found = 0, missed = 0;
        for (String xPathStatement : xPaths) {
            System.out.println("Working on xPath: " + xPathStatement);
            xpath.setNamespaceContext(new extractTB3OntologyProperties());
            XPathExpression expr = xpath.compile(xPathStatement);
            Object result = expr.evaluate(TB3XMLdoc, XPathConstants.NODESET);
            NodeList nodes = (NodeList) result;
            System.out.println("nodes: " + nodes.getLength());
            for (int i = 0; i < nodes.getLength(); i++) {
                String name = null, comment = null;
                NamedNodeMap refAttributes = nodes.item(i).getAttributes();
                for (int k = 0; k < refAttributes.getLength(); k++) {
                    if (refAttributes.item(k).getNodeName().equals("rdf:about")) {
                        name = refAttributes.item(k).getNodeValue();
                        if (name.indexOf("#") != -1) {
                            name = name.substring(name.indexOf("#") + 1);
                        }
                    }
                }
                NodeList childnodes = nodes.item(i).getChildNodes();
                for (int p = 0; p < childnodes.getLength(); p++) {
                    if (childnodes.item(p).getNodeName().equals("rdfs:comment")) {
                        comment = childnodes.item(p).getTextContent();
                    }
                }
                if ((name != null) && (comment != null)) {
                    found++;
                    String uri = "planets://testbed/properties/ontology/" + name;
                    writer.write("<property tburi=\"" + uri + "\">\n");
                    writer.write("<name>" + name + "</name>\n");
                    writer.write("<description>" + comment + "</description>\n");
                    writer.write("</property>\n");
                } else {
                    missed++;
                }
            }
        }
        writer.write("</extractedTB3ontologyProperties>");
        writer.close();
        System.out.println("nr of properties found: " + found + " missed: " + missed);
    }
