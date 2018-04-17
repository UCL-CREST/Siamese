    private void parseCapabilities(String data) throws Exception {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        ByteArrayInputStream reader = new ByteArrayInputStream(data.toString().getBytes());
        Document doc = docBuilder.parse(reader);
        NodeList capabilities = doc.getElementsByTagName("capability");
        for (int x = 0; x < capabilities.getLength(); x++) {
            Node capability = capabilities.item(x);
            NodeList nl = capability.getChildNodes();
            String name = "";
            int id = -1;
            String ext = "";
            for (int y = 0; y < nl.getLength(); y++) {
                Node item = nl.item(y);
                if (item.getNodeName().equals("name")) {
                    name = item.getTextContent();
                } else if (item.getNodeName().equals("id")) {
                    id = Integer.parseInt(item.getTextContent());
                } else if (item.getNodeName().equals("ext")) {
                    ext = item.getTextContent();
                }
            }
            if (name.length() > 0 && id != -1 && ext.length() > 0) {
                this.capabilities.add(new CaptureCapability(name, id, ext));
            }
        }
    }
