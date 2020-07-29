    public TextResource(DefaultValuesProvider parentDVP, URL resourceURL, File metadataFile) throws Exception {
        super(parentDVP, resourceURL, metadataFile);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        DocumentBuilder b = dbf.newDocumentBuilder();
        String stripped = new String();
        String file = resourceURL.getFile();
        if (file.endsWith(".text")) {
            doc = b.parse(resourceURL.openStream());
            Element root = doc.getDocumentElement();
            NodeIterator ni = ((org.apache.xerces.dom.DocumentImpl) doc).createNodeIterator(root, NodeFilter.SHOW_TEXT, null, false);
            boolean first = true;
            for (Node n = ni.nextNode(); n != null; n = ni.nextNode()) {
                stripped += ((Text) n).getData();
            }
        } else {
            InputStream is = resourceURL.openStream();
            byte[] buf = new byte[1024];
            is.read(buf, 0, 1024);
            stripped = new String(buf);
        }
        Font baseFont = new Font(null, Font.PLAIN, 10);
        smallIcon = new TextIcon(baseFont.deriveFont(4), stripped, 64, 64);
        if (file.endsWith(".html")) {
            JEditorPane prev = new JEditorPane(resourceURL);
            prev.setEditable(false);
            preview = new JScrollPane(prev);
            preview.setSize(256, 256);
            preview.setPreferredSize(new Dimension(256, 256));
            preview.setMinimumSize(new Dimension(256, 256));
        } else {
            preview = new JLabel(new TextIcon(baseFont.deriveFont(8), stripped, 256, 256));
        }
    }
