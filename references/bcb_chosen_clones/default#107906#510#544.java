    public static void exampleDOMtoDOMNew(String sourceID, String xslID) throws TransformerException, TransformerConfigurationException, SAXException, IOException, ParserConfigurationException, MalformedURLException {
        TransformerFactory tfactory = TransformerFactory.newInstance();
        if (tfactory.getFeature(DOMSource.FEATURE)) {
            Templates templates;
            {
                System.setProperty("javax.xml.parsers.DocumentBuilderFactory", "com.icl.saxon.om.DocumentBuilderFactoryImpl");
                DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
                System.err.println("Using DocumentBuilderFactory " + dfactory.getClass());
                dfactory.setNamespaceAware(true);
                DocumentBuilder docBuilder = dfactory.newDocumentBuilder();
                System.err.println("Using DocumentBuilder " + docBuilder.getClass());
                org.w3c.dom.Document outNode = docBuilder.newDocument();
                Node doc = docBuilder.parse(new InputSource(new File(xslID).toURL().toString()));
                System.err.println("Stylesheet document built OK");
                DOMSource dsource = new DOMSource(doc);
                dsource.setSystemId(new File(xslID).toURL().toString());
                templates = tfactory.newTemplates(dsource);
            }
            Transformer transformer = templates.newTransformer();
            DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dfactory.newDocumentBuilder();
            Node doc = docBuilder.parse(new InputSource(new File(sourceID).toURL().toString()));
            System.err.println("Source document built OK");
            DOMSource ds = new DOMSource(doc);
            DOMResult result = new DOMResult();
            transformer.transform(ds, result);
            System.err.println("Transformation done OK");
            Transformer serializer = tfactory.newTransformer();
            serializer.transform(new DOMSource(result.getNode()), new StreamResult(System.out));
            int k = result.getNode().getChildNodes().getLength();
            System.err.println("Result root has " + k + " children");
        } else {
            throw new org.xml.sax.SAXNotSupportedException("DOM node processing not supported!");
        }
    }
