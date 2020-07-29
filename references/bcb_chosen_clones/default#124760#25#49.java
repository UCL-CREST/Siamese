    public static void main(String[] args) throws TransformerException, TransformerConfigurationException, FileNotFoundException, ParserConfigurationException, SAXException, IOException {
        TransformerFactory tFactory = TransformerFactory.newInstance();
        if (tFactory.getFeature(DOMSource.FEATURE) && tFactory.getFeature(DOMResult.FEATURE)) {
            DocumentBuilderFactory dFactory = DocumentBuilderFactory.newInstance();
            dFactory.setNamespaceAware(true);
            DocumentBuilder dBuilder = dFactory.newDocumentBuilder();
            Document xslDoc = dBuilder.parse("birds.xsl");
            DOMSource xslDomSource = new DOMSource(xslDoc);
            xslDomSource.setSystemId("birds.xsl");
            Transformer transformer = tFactory.newTransformer(xslDomSource);
            Document xmlDoc = dBuilder.parse("birds.xml");
            DOMSource xmlDomSource = new DOMSource(xmlDoc);
            xmlDomSource.setSystemId("birds.xml");
            DOMResult domResult = new DOMResult();
            transformer.transform(xmlDomSource, domResult);
            java.util.Properties xmlProps = OutputPropertiesFactory.getDefaultMethodProperties("xml");
            xmlProps.setProperty("indent", "yes");
            xmlProps.setProperty("standalone", "no");
            Serializer serializer = SerializerFactory.getSerializer(xmlProps);
            serializer.setOutputStream(System.out);
            serializer.asDOMSerializer().serialize(domResult.getNode());
        } else {
            throw new org.xml.sax.SAXNotSupportedException("DOM node processing not supported!");
        }
    }
