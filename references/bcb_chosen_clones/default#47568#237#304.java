    public static void main(String[] args) throws Exception {
        String filename = null;
        boolean dtdValidate = false;
        boolean xsdValidate = false;
        String schemaSource = null;
        boolean ignoreWhitespace = false;
        boolean ignoreComments = false;
        boolean putCDATAIntoText = false;
        boolean createEntityRefs = false;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-dtd")) {
                dtdValidate = true;
            } else if (args[i].equals("-xsd")) {
                xsdValidate = true;
            } else if (args[i].equals("-xsdss")) {
                if (i == args.length - 1) {
                    usage();
                }
                xsdValidate = true;
                schemaSource = args[++i];
            } else if (args[i].equals("-ws")) {
                ignoreWhitespace = true;
            } else if (args[i].startsWith("-co")) {
                ignoreComments = true;
            } else if (args[i].startsWith("-cd")) {
                putCDATAIntoText = true;
            } else if (args[i].startsWith("-e")) {
                createEntityRefs = true;
            } else if (args[i].equals("-usage")) {
                usage();
            } else if (args[i].equals("-help")) {
                usage();
            } else {
                filename = args[i];
                if (i != args.length - 1) {
                    usage();
                }
            }
        }
        if (filename == null) {
            usage();
        }
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        dbf.setValidating(dtdValidate || xsdValidate);
        if (xsdValidate) {
            try {
                dbf.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
            } catch (IllegalArgumentException x) {
                System.err.println("Error: JAXP DocumentBuilderFactory attribute not recognized: " + JAXP_SCHEMA_LANGUAGE);
                System.err.println("Check to see if parser conforms to JAXP 1.2 spec.");
                System.exit(1);
            }
        }
        if (schemaSource != null) {
            dbf.setAttribute(JAXP_SCHEMA_SOURCE, new File(schemaSource));
        }
        dbf.setIgnoringComments(ignoreComments);
        dbf.setIgnoringElementContentWhitespace(ignoreWhitespace);
        dbf.setCoalescing(putCDATAIntoText);
        dbf.setExpandEntityReferences(!createEntityRefs);
        DocumentBuilder db = dbf.newDocumentBuilder();
        OutputStreamWriter errorWriter = new OutputStreamWriter(System.err, outputEncoding);
        db.setErrorHandler(new MyErrorHandler(new PrintWriter(errorWriter, true)));
        Document doc = db.parse(new File(filename));
        OutputStreamWriter outWriter = new OutputStreamWriter(System.out, outputEncoding);
        new csearch(new PrintWriter(outWriter, true)).echo(doc);
    }
