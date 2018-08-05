    public static void main(String[] args) {
        try {
            DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
            fact.setNamespaceAware(false);
            fact.setValidating(false);
            Transformer trs = TransformerFactory.newInstance().newTransformer(new StreamSource("xslt/importStruts2.xsl"));
            DocumentBuilder builder = fact.newDocumentBuilder();
            builder.setEntityResolver(new LocalEntityResolver());
            Document src = builder.parse(new File("/home/sylvain/struts.xml"));
            trs.transform(new DOMSource(src), new StreamResult(new File("/home/sylvain/target.xml")));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
