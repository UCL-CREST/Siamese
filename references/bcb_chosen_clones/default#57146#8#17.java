    public static String getRootTagName(String XMLexcerpt) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            Document d = factory.newDocumentBuilder().parse(new InputSource(new StringReader(XMLexcerpt)));
            Node n = d.getFirstChild();
            return n.getNodeName();
        } catch (Exception e) {
            return null;
        }
    }
