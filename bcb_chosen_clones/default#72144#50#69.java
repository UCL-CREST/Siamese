    private static void initDocument() {
        DocumentBuilder builder;
        DocumentBuilderFactory factory;
        NodeList list;
        String message;
        try {
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            document = builder.parse(filename);
        } catch (ParserConfigurationException pce) {
            message = "[Configuration] Cannot create XML parser.";
            throw new ExceptionInInitializerError(message);
        } catch (SAXException se) {
            message = "[Configuration] Cannot parse configuration file.";
            throw new ExceptionInInitializerError(message);
        } catch (IOException ie) {
            message = "[Configuration] Cannot open configuration file.";
            throw new ExceptionInInitializerError(message);
        }
    }
