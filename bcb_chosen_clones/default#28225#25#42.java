    public Metadata(DefaultValuesProvider dvp, File file) throws Exception {
        this.dvp = dvp;
        this.file = file;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        DocumentBuilder b = dbf.newDocumentBuilder();
        if (file.exists()) {
            doc = b.parse(file);
            changed = false;
        } else {
            doc = b.newDocument();
            Element root = doc.createElement("metadata");
            doc.appendChild(root);
            changed = true;
        }
        root = doc.getDocumentElement();
        lang = DEFAULT_LANG;
    }
