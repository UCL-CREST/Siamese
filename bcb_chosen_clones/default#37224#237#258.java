    private void loadDB() {
        try {
            doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
            xpath = XPathFactory.newInstance().newXPath();
            ch = new ContentHandler() {

                @Override
                public Object getContent(URLConnection urlc) throws IOException {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
            };
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace(System.err);
            System.exit(-1);
        } catch (SAXException saxe) {
            saxe.printStackTrace(System.err);
            System.exit(-1);
        } catch (IOException ioe) {
            ioe.printStackTrace(System.err);
            System.exit(-1);
        }
    }
