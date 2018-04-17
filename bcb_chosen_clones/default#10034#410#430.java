    public void importMatchList(String data, boolean append) throws Exception {
        HashMap<String, EpgMatchList> importedMatchList = new HashMap<String, EpgMatchList>();
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        ByteArrayInputStream reader = new ByteArrayInputStream(data.toString().getBytes());
        Document doc = docBuilder.parse(reader);
        NodeList items = doc.getElementsByTagName("MatchList");
        for (int x = 0; x < items.getLength(); x++) {
            Node item = items.item(x);
            NamedNodeMap itemAttribs = item.getAttributes();
            String name = itemAttribs.getNamedItem("name").getTextContent();
            EpgMatchList matcher = new EpgMatchList(item);
            importedMatchList.put(name, matcher);
        }
        if (append) {
            epgMatchLists.putAll(importedMatchList);
        } else {
            epgMatchLists = importedMatchList;
        }
        saveMatchList(null);
    }
