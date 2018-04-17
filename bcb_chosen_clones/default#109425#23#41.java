    public List<BambooBuild> loadRssFeed() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        System.out.println(this.rssUrl);
        Document doc = builder.parse(this.rssUrl);
        NodeList items = doc.getElementsByTagName("item");
        List<BambooBuild> builds = new ArrayList<BambooBuild>();
        for (int i = 0; i < items.getLength(); i++) {
            Element item = (Element) items.item(i);
            NodeList titleNodeList = item.getElementsByTagName("title");
            String title = titleNodeList.item(0).getTextContent();
            NodeList dateNodeList = item.getElementsByTagName("dc:date");
            String dateStr = dateNodeList.item(0).getTextContent();
            Date date = DateUtils.parseIso8601DateTime(dateStr);
            BambooBuild bb = this.createBambooBuild(title, date);
            builds.add(bb);
        }
        return builds;
    }
