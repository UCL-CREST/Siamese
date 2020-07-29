    private void loadChannels() {
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new File(this.getProperty("path.data") + File.separator + "Channels.xml"));
            NodeList items = doc.getElementsByTagName("channel");
            channels = new HashMap<String, Channel>();
            for (int x = 0; x < items.getLength(); x++) {
                Node item = items.item(x);
                Channel chan = new Channel(item);
                channels.put(chan.getName(), chan);
            }
            System.out.println("Channels.xml found and loaded (" + items.getLength() + ")");
        } catch (Exception e) {
            channels = new HashMap<String, Channel>();
            System.out.println("Error loading Channels.xml, starting with blank channel list.");
        }
    }
