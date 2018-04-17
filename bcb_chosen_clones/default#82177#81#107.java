    private Document getDocument(URL url) throws SAXException, IOException {
        InputStream is;
        try {
            is = url.openStream();
        } catch (IOException io) {
            System.out.println("parameter error : The specified reading data is mistaken.");
            System.out.println(" Request URL is " + sourceUri);
            throw new IOException("\t" + io.toString());
        }
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException pce) {
            System.out.println("error : The error of DocumentBuilder instance generation");
            throw new RuntimeException(pce.toString());
        }
        Document doc;
        try {
            doc = builder.parse(is);
        } catch (Exception e) {
            System.out.println("error : parse of reading data went wrong.");
            System.out.println(" Request URL is " + sourceUri);
            throw new RuntimeException(e.toString());
        }
        return doc;
    }
