    private void loadEpgAutoList() {
        epgMatchList = new Vector<EpgMatch>();
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new File(this.getProperty("path.data") + File.separator + "EpgAutoAdd.xml"));
            NodeList items = doc.getElementsByTagName("item");
            for (int x = 0; x < items.getLength(); x++) {
                Node item = items.item(x);
                EpgMatch matcher = new EpgMatch(item);
                epgMatchList.add(matcher);
            }
            System.out.println("EpgAutoAdd.xml found and loaded (" + items.getLength() + ")");
        } catch (Exception e) {
            epgMatchList = new Vector<EpgMatch>();
            System.out.println("Error loading EpgAutoAdd.xml, starting with no AutoAdds");
        }
    }
