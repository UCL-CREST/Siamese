    public ViewParser() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder bilder = factory.newDocumentBuilder();
        Document root = bilder.parse(new File("partner_view.xml"));
        System.out.println(root.getFirstChild().getNodeName());
        root.getElementsByTagName("record");
        this.processRecords(root.getElementsByTagName("record"));
    }
