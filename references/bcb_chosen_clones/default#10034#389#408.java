    private void loadMatchList() {
        epgMatchLists = new HashMap<String, EpgMatchList>();
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new File(this.getProperty("path.data") + File.separator + "MatchList.xml"));
            NodeList items = doc.getElementsByTagName("MatchList");
            for (int x = 0; x < items.getLength(); x++) {
                Node item = items.item(x);
                NamedNodeMap itemAttribs = item.getAttributes();
                String name = itemAttribs.getNamedItem("name").getTextContent();
                EpgMatchList matcher = new EpgMatchList(item);
                epgMatchLists.put(name, matcher);
            }
            System.out.println("MatchList.xml found and loaded (" + epgMatchLists.size() + ")");
        } catch (Exception e) {
            epgMatchLists = new HashMap<String, EpgMatchList>();
            System.out.println("Error loading MatchList.xml, starting with no Match Lists");
        }
    }
