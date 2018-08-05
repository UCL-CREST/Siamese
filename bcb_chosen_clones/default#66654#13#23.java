    public static void main(String[] args) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File("DomainModel.xml"));
            Element ele = doc.getDocumentElement();
            System.out.println(ele.getTagName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
