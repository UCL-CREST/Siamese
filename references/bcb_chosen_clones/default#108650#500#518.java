    public void importEpgAutoList(String data, boolean append) throws Exception {
        Vector<EpgMatch> importedData = new Vector<EpgMatch>();
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        ByteArrayInputStream reader = new ByteArrayInputStream(data.toString().getBytes());
        Document doc = docBuilder.parse(reader);
        NodeList items = doc.getElementsByTagName("item");
        for (int x = 0; x < items.getLength(); x++) {
            Node item = items.item(x);
            EpgMatch matcher = new EpgMatch(item);
            importedData.add(matcher);
        }
        if (append) {
            epgMatchList.addAll(importedData);
        } else {
            epgMatchList = importedData;
        }
        saveEpgAutoList(null);
    }
