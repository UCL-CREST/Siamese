    public static Node LoadRoot(String path) {
        DocumentBuilderFactory factory = null;
        DocumentBuilder builder = null;
        File xmlFile = null;
        Document doc = null;
        try {
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            xmlFile = new File(path);
            doc = builder.parse(xmlFile);
        } catch (Exception Ex1) {
            System.out.println(Ex1.toString());
            System.exit(1);
        }
        return doc;
    }
