    private void showPage(String which, HttpServletRequest req, HttpServletResponse res) throws IOException, ParserConfigurationException, SAXException, TransformerException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(getClass().getResourceAsStream("/skeleton.xml"));
        Document nav = db.parse(getClass().getResourceAsStream("/navigation.xml"));
        Document page = db.parse(CDoxWeb.class.getResourceAsStream(which));
        Node body = doc.getElementsByTagName("body").item(0);
        Node pdiv = doc.importNode(page.getElementsByTagName("div").item(0), true);
        Node ndiv = doc.importNode(nav.getElementsByTagName("div").item(0), true);
        body.appendChild(ndiv);
        body.appendChild(pdiv);
        sendDocument(doc, new PrintWriter(res.getWriter()), req, res);
    }
