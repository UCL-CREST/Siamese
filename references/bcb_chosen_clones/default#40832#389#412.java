    public int loadXMLdata(byte[] xmlFile) throws Exception {
        int found = 0;
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        ByteArrayInputStream reader = new ByteArrayInputStream(xmlFile);
        Document doc = docBuilder.parse(reader);
        NodeList root = null;
        root = doc.getElementsByTagName("tv");
        if (root != null) {
            Node firstNode = root.item(0);
            if (firstNode == null) {
                System.out.println("ERROR precessing XML data, first node of <tv> not found.");
                return found;
            }
            root = firstNode.getChildNodes();
            if (root == null) {
                System.out.println("ERROR precessing XML data, no children of first node.");
                return found;
            }
            HashMap channels = loadChannels(root);
            found = recurseXMLData(root, channels);
        }
        return found;
    }
