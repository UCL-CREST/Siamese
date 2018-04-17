    public static void main(String argv[]) throws IOException, DOMException, ParserConfigurationException {
        if (argv.length < 1) {
            System.out.println("Usage: java DomDocumentPrinter filename.xml [otherFiles.xml]");
            System.exit(0);
        }
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        documentBuilderFactory.setValidating(false);
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        X3dCustomHandler handler = new X3dCustomHandler();
        documentBuilder.setEntityResolver(handler);
        documentBuilder.setErrorHandler(handler);
        Document document = documentBuilder.newDocument();
        DomDocumentPrinter domDocumentPrinter = new DomDocumentPrinter();
        for (int i = 0; i < argv.length; i++) {
            String fileName = argv[i];
            try {
                if (argv.length > 1) {
                    System.out.println("\nParsing " + fileName + "\n");
                }
                handler.setInputSource(new InputSource(fileName));
                document = documentBuilder.parse(fileName);
            } catch (Exception badParse) {
                System.out.println("Error in parsing: " + badParse.getMessage());
                badParse.printStackTrace();
            }
            domDocumentPrinter.printNode(document);
        }
    }
