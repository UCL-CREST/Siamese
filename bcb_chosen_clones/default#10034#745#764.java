    public void importChannels(String data, boolean append) throws Exception {
        HashMap<String, Channel> importedChannels = new HashMap<String, Channel>();
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        ByteArrayInputStream reader = new ByteArrayInputStream(data.toString().getBytes());
        Document doc = docBuilder.parse(reader);
        NodeList items = doc.getElementsByTagName("channel");
        for (int x = 0; x < items.getLength(); x++) {
            Node item = items.item(x);
            Channel chan = new Channel(item);
            importedChannels.put(chan.getName(), chan);
        }
        if (append) {
            if (channels == null) channels = new HashMap<String, Channel>();
            channels.putAll(importedChannels);
        } else {
            channels = importedChannels;
        }
        saveChannels(null);
    }
