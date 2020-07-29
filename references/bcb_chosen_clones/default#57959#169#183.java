    private static Document getDocument(String name) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringComments(true);
            factory.setIgnoringElementContentWhitespace(true);
            factory.setValidating(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(new InputSource(name));
        } catch (Exception e) {
            System.out.println("The xml file " + name + " couldn't be processed. Read further for details.");
            System.out.println(e.getMessage());
            System.exit(0);
        }
        return null;
    }
