    private void loadEpgWatchList() {
        epgWatchList = new Vector<String>();
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new File(this.getProperty("path.data") + File.separator + "EpgWatchList.xml"));
            NodeList items = doc.getElementsByTagName("item");
            for (int x = 0; x < items.getLength(); x++) {
                Node item = items.item(x);
                String itemText = item.getTextContent();
                epgWatchList.add(itemText);
            }
            System.out.println("EpgWatchList.xml found and loaded (" + items.getLength() + ")");
        } catch (Exception e) {
            epgWatchList = new Vector<String>();
            System.out.println("Error loading EpgWatchList.xml, starting with no Watch List");
        }
    }
