    public GalleryResource(DefaultValuesProvider parentDVP, File resourceFile, File metadataFile) throws Exception {
        super(parentDVP, resourceFile, metadataFile);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        DocumentBuilder b = dbf.newDocumentBuilder();
        doc = b.parse(resourceFile);
        Element root = doc.getDocumentElement();
        NodeList nl = root.getChildNodes();
        String stripped = new String();
        boolean first = true;
        for (int i = 0; i < nl.getLength(); i++) {
            Node n = nl.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) n;
                stripped += e.getTagName() + ": " + e.getAttribute("lcref") + "\n";
            }
        }
        Font baseFont = new Font(null, Font.PLAIN, 10);
        smallIcon = new TextIcon(baseFont.deriveFont(4), stripped, 64, 64);
        bigIcon = new TextIcon(baseFont.deriveFont(8), stripped, 256, 256);
    }
