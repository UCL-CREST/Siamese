    public static Node exampleDOM2DOM(String sourceID, String xslID) throws TransformerException, TransformerConfigurationException, SAXException, IOException, ParserConfigurationException {
        TransformerFactory tfactory = TransformerFactory.newInstance();
        if (tfactory.getFeature(DOMSource.FEATURE)) {
            Templates templates;
            {
                DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
                dfactory.setNamespaceAware(true);
                DocumentBuilder docBuilder = dfactory.newDocumentBuilder();
                org.w3c.dom.Document outNode = docBuilder.newDocument();
                Node doc = docBuilder.parse(new InputSource(xslID));
                DOMSource dsource = new DOMSource(doc);
                dsource.setSystemId(xslID);
                templates = tfactory.newTemplates(dsource);
            }
            Transformer transformer = templates.newTransformer();
            DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
            dfactory.setNamespaceAware(true);
            DocumentBuilder docBuilder = dfactory.newDocumentBuilder();
            org.w3c.dom.Document outNode = docBuilder.newDocument();
            Node doc = docBuilder.parse(new InputSource(sourceID));
            transformer.transform(new DOMSource(doc), new DOMResult(outNode));
            Transformer serializer = tfactory.newTransformer();
            serializer.transform(new DOMSource(outNode), new StreamResult(new OutputStreamWriter(System.out)));
            return outNode;
        } else {
            throw new org.xml.sax.SAXNotSupportedException("DOM node processing not supported!");
        }
    }
