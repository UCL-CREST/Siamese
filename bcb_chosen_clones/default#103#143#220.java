    public static Hashtable parse_RSS_news_data() throws javax.xml.parsers.ParserConfigurationException, org.xml.sax.SAXException, java.io.IOException {
        System.out.println("Start RSS Parser");
        DocumentBuilderFactory document_builder_factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder document_builder = document_builder_factory.newDocumentBuilder();
        Document document = document_builder.parse(new InputSource(new StringReader(URLGrabber.getDocumentAsString("http://www.comune.torino.it/cgi-bin/torss/rssfeed.cgi?id=93"))));
        Node node;
        Node a_node;
        Node an_inner_node;
        Node an_inner_inner_node;
        NodeList node_list;
        NodeList inner_node_list;
        NodeList inner_inner_node_list;
        node = document.getFirstChild();
        node_list = node.getChildNodes();
        String channel_title = "";
        String channel_link = "";
        String channel_description = "";
        String channel_copyright = "";
        String channel_pubDate = "";
        String channel_category = "";
        String item_title = "";
        String item_link = "";
        String item_pubDate = "";
        String item_description = "";
        String unit = "";
        String lat = "0";
        String lng = "0";
        Hashtable rss_map = new Hashtable();
        ArrayList result = new ArrayList();
        System.out.println("[0] - [" + node.getNodeName() + "]");
        for (int i = 0; i < node_list.getLength(); i++) {
            a_node = node_list.item(i);
            System.out.println("  [1] - [" + a_node.getNodeName() + "]");
            if (a_node.getNodeType() == Node.ELEMENT_NODE) {
                inner_node_list = a_node.getChildNodes();
                for (int j = 0; j < inner_node_list.getLength(); j++) {
                    an_inner_node = inner_node_list.item(j);
                    if ((!an_inner_node.getNodeName().equals("item")) && (!an_inner_node.getNodeName().equals("#text"))) {
                        System.out.println("    [2] " + an_inner_node.getNodeName() + ": " + an_inner_node.getTextContent());
                        if (an_inner_node.getNodeName().equals("title")) channel_title = an_inner_node.getTextContent();
                        if (an_inner_node.getNodeName().equals("link")) channel_link = an_inner_node.getTextContent();
                        if (an_inner_node.getNodeName().equals("description")) channel_description = an_inner_node.getTextContent();
                        if (an_inner_node.getNodeName().equals("copyright")) channel_copyright = an_inner_node.getTextContent();
                        if (an_inner_node.getNodeName().equals("pubDate")) channel_pubDate = an_inner_node.getTextContent();
                        if (an_inner_node.getNodeName().equals("category")) channel_category = an_inner_node.getTextContent();
                    }
                    if (an_inner_node.getNodeName().equals("item")) {
                        System.out.println("    [2] - [" + an_inner_node.getNodeName() + "]");
                    }
                    if (an_inner_node.hasChildNodes() && (an_inner_node.getNodeName().equals("item"))) {
                        inner_inner_node_list = an_inner_node.getChildNodes();
                        for (int k = 0; k < inner_inner_node_list.getLength(); k++) {
                            an_inner_inner_node = inner_inner_node_list.item(k);
                            if (!(an_inner_inner_node.getTextContent().equals(""))) {
                                if (!(an_inner_inner_node.getNodeName().equals("#text")) && !(an_inner_inner_node.getNodeName().equals("link"))) {
                                    System.out.println("        [3] " + an_inner_inner_node.getNodeName() + ": " + an_inner_inner_node.getTextContent());
                                    if (an_inner_inner_node.getNodeName().equals("title")) item_title = an_inner_inner_node.getTextContent();
                                    if (an_inner_inner_node.getNodeName().equals("pubDate")) item_pubDate = an_inner_inner_node.getTextContent();
                                    if (an_inner_inner_node.getNodeName().equals("description")) item_description = an_inner_inner_node.getTextContent();
                                }
                                if (an_inner_inner_node.getNodeName().equals("link")) {
                                    String tmp_link = an_inner_inner_node.getTextContent();
                                    tmp_link = tmp_link.replace("http://www.torinocultura.it/servizionline/memento/include.php?urlDest=", "");
                                    System.out.println("        [3] " + an_inner_inner_node.getNodeName() + ": " + tmp_link);
                                    if (an_inner_inner_node.getNodeName().equals("link")) item_link = an_inner_inner_node.getTextContent();
                                }
                            }
                        }
                        unit = "Unit data: [" + item_title + " - " + item_pubDate + " - " + item_description + " - " + item_link + "]";
                        result.add(unit);
                        rss_map.put(item_title, unit);
                    }
                    System.out.println("--------------");
                }
            }
        }
        return rss_map;
    }
