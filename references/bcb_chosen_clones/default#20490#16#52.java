    public static void main(String[] args) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse("config.xml");
            Node config = document.getDocumentElement();
            NodeList sectionList = config.getChildNodes();
            for (int i = 0; i < sectionList.getLength(); i++) {
                Node section = sectionList.item(i);
                if (!"section".equals(section.getNodeName())) continue;
                System.out.println("============ " + findAttribute(section, "name") + " ================");
                NodeList paramList = section.getChildNodes();
                for (int j = 0; j < paramList.getLength(); j++) {
                    Node parameter = paramList.item(j);
                    if (!"parameter".equals(parameter.getNodeName())) continue;
                    String name = findAttribute(parameter, "name");
                    String value = findAttribute(parameter, "value");
                    System.out.println(name + "=" + value);
                    Node attr = document.createAttribute("value");
                    attr.setNodeValue("test");
                    parameter.getAttributes().setNamedItem(attr);
                }
                Node par = document.createElement("neu");
                Node attr = document.createAttribute("value");
                attr.setNodeValue("test-neu");
                par.getAttributes().setNamedItem(attr);
                attr = document.createAttribute("name");
                attr.setNodeValue("neu");
                par.getAttributes().setNamedItem(attr);
                section.appendChild(par);
            }
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(document), new StreamResult(System.out));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
