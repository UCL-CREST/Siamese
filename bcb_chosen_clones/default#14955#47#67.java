    private void parseXmlFile(File f) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            if (f.exists()) {
                dom = db.parse(f);
            } else {
                try {
                    throw new FileNotFoundException(f.getName() + " Not found");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
