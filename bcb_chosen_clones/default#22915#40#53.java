    private void showDownloadPage(HttpServletRequest req, HttpServletResponse res) throws IOException, ParserConfigurationException, SAXException, TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t = tf.newTransformer(new StreamSource(getClass().getResourceAsStream("/rss2html.xsl")));
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(getClass().getResourceAsStream("/skeleton.xml"));
        Document nav = db.parse(getClass().getResourceAsStream("/navigation.xml"));
        Node body = doc.getElementsByTagName("body").item(0);
        Node ndiv = doc.importNode(nav.getElementsByTagName("div").item(0), true);
        body.appendChild(ndiv);
        DOMResult result = new DOMResult(body);
        t.transform(new StreamSource("http://sourceforge.net/export/rss2_projfiles.php?group_id=48318"), result);
        sendDocument(doc, new PrintWriter(res.getWriter()), req, res);
    }
