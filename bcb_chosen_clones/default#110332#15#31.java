    public static void main(String[] args) {
        try {
            String xmlFile = "test.xml";
            String schemaFile = "jxpl_0_1.xsd";
            if (args.length > 0 && !"${xml}".equals(args[0])) xmlFile = args[0];
            if (args.length > 1 && !"${schema}".equals(args[1])) schemaFile = args[1];
            DocumentBuilderFactory bfactory = DocumentBuilderFactory.newInstance();
            bfactory.setNamespaceAware(true);
            bfactory.setValidating(true);
            bfactory.setAttribute(JAXP_SCHEMA_SOURCE, new File(schemaFile));
            bfactory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
            DocumentBuilder builder = bfactory.newDocumentBuilder();
            Document doc = builder.parse(new File(xmlFile));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
