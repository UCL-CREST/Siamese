    public static void main(String argv[]) {
        if (argv.length != 3) {
            System.err.println("usage: java Transform xmlin xslt xmlout");
            System.exit(0);
        }
        try {
            DocumentBuilderFactory docBuildFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder parser = docBuildFactory.newDocumentBuilder();
            Document document = parser.parse(new File(argv[0]));
            TransformerFactory xformFactory = TransformerFactory.newInstance();
            Transformer transformer = xformFactory.newTransformer(new StreamSource(new File(argv[1])));
            DOMSource source = new DOMSource(document);
            StreamResult scrResult = new StreamResult(System.out);
            transformer.transform(source, scrResult);
            PrintWriter outStream = new PrintWriter(new FileOutputStream(argv[2]));
            StreamResult fileResult = new StreamResult(outStream);
            transformer.transform(source, fileResult);
        } catch (SAXParseException saxEx) {
            System.err.println("\nSAXParseException");
            System.err.println("Public ID: " + saxEx.getPublicId());
            System.err.println("System ID: " + saxEx.getSystemId());
            System.err.println("Line: " + saxEx.getLineNumber());
            System.err.println("Column:" + saxEx.getColumnNumber());
            System.err.println(saxEx.getMessage());
            Exception ex = saxEx;
            if (saxEx.getException() != null) {
                ex = saxEx.getException();
                System.err.println(ex.getMessage());
            }
        } catch (SAXException saxEx) {
            System.err.println("\nParser Error");
            System.err.println(saxEx.getMessage());
            Exception ex = saxEx;
            if (saxEx.getException() != null) {
                ex = saxEx.getException();
                System.err.println(ex.getMessage());
            }
        } catch (ParserConfigurationException parConEx) {
            System.err.println("\nParser Config Error");
            System.err.println(parConEx.getMessage());
        } catch (TransformerConfigurationException transConEx) {
            System.err.println("\nTransformer Config Error");
            System.err.println(transConEx.getMessage());
            Throwable ex = transConEx;
            ex.printStackTrace();
            if (transConEx.getException() != null) {
                ex = transConEx.getException();
                System.err.println(ex.getMessage());
            }
        } catch (TransformerException transEx) {
            System.err.println("\nTransformation error");
            System.err.println(transEx.getMessage());
            Throwable ex = transEx;
            if (transEx.getException() != null) {
                ex = transEx.getException();
                System.err.println(ex.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
