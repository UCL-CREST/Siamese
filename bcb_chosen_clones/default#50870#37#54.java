    public void validate(final File file) {
        try {
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(true);
            final DocumentBuilder builder = factory.newDocumentBuilder();
            final DomValidatorErrorHandler handler = new DomValidatorErrorHandler();
            handler.setErrorFile(errorFile);
            builder.setErrorHandler(handler);
            final Document document = builder.parse(file);
            setErrorCount(handler.getErrorCount());
        } catch (ParserConfigurationException e) {
            System.out.println(e.toString());
        } catch (SAXException e) {
            System.out.println(e.toString());
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }
