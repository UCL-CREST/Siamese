    private static Document readFromStream(InputStream is) {
        Document document = null;
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            document = docBuilder.parse(is);
            document.getDocumentElement().normalize();
        } catch (SAXParseException err) {
            String msg = "** SAXParseException" + ", line " + err.getLineNumber() + ", uri " + err.getSystemId() + "\n" + "   " + err.getMessage();
            System.err.println(msg);
            Exception x = err.getException();
            ((x == null) ? err : x).printStackTrace();
        } catch (SAXException e) {
            String msg = "SAXException";
            System.err.println(msg);
            Exception x = e.getException();
            ((x == null) ? e : x).printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return document;
    }
