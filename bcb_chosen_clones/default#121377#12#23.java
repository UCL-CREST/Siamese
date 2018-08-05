    public static void main(String[] args) {
        try {
            DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
            fact.setNamespaceAware(true);
            Transformer trs = TransformerFactory.newInstance().newTransformer(new StreamSource("xslt/importStruts2.xsl"));
            Document src = fact.newDocumentBuilder().parse(new File("/home/sylvain/struts.xml"));
            trs.transform(new DOMSource(src), new StreamResult(new File("target.xml")));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
