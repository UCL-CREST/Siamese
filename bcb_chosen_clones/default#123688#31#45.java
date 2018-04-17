    public Traitement_Serveur(ObjetServeur ser) {
        serveur = ser;
        DocumentBuilder db;
        try {
            db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            base_msg = db.parse(new File("./bd_msg.xml"));
            xpath_msg = javax.xml.xpath.XPathFactory.newInstance().newXPath();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
