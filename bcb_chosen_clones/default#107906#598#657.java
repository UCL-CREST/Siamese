    public static void exampleDOMtoDOMAlien(String sourceID, String xslID) throws TransformerException, TransformerConfigurationException, SAXException, IOException, ParserConfigurationException, MalformedURLException {
        String factory = null;
        try {
            Class.forName("org.apache.crimson.jaxp.DocumentBuilderFactoryImpl");
            factory = "org.apache.crimson.jaxp.DocumentBuilderFactoryImpl";
        } catch (Exception e) {
            factory = null;
        }
        if (factory == null) {
            try {
                Class.forName("org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
                factory = "org.apache.xerces.jaxp.DocumentBuilderFactoryImpl";
            } catch (Exception e) {
                factory = null;
            }
        }
        if (factory == null) {
            System.err.println("No third-party DOM Builder found");
            return;
        }
        System.setProperty("javax.xml.parsers.DocumentBuilderFactory", factory);
        TransformerFactory tfactory = TransformerFactory.newInstance();
        if (tfactory.getFeature(DOMSource.FEATURE)) {
            Templates templates;
            {
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
            Document doc = docBuilder.parse(new InputSource(new File(sourceID).toURL().toString()));
            Node bar = doc.getDocumentElement().getFirstChild();
            while (bar.getNodeType() != Node.ELEMENT_NODE) {
                bar = bar.getNextSibling();
            }
            System.err.println("Source document built OK");
            DOMSource ds = new DOMSource(bar);
            ds.setSystemId(new File(sourceID).toURL().toString());
            Document out = docBuilder.newDocument();
            Element extra = out.createElement("extra");
            out.appendChild(extra);
            transformer.transform(ds, new DOMResult(extra));
            System.err.println("Transformation done OK");
            Transformer serializer = tfactory.newTransformer();
            serializer.transform(new DOMSource(out), new StreamResult(System.out));
            return;
        } else {
            throw new org.xml.sax.SAXNotSupportedException("DOM node processing not supported!");
        }
    }
