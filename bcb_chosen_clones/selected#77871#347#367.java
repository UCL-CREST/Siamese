    public ExtentManager(Document doc) {
        Element root = (Element) (doc.getElementsByTagName("ROOT").item(0));
        NodeList extentsList = root.getElementsByTagName("Extent");
        for (int i = 0; i < extentsList.getLength(); i++) {
            Element ext_elem = (Element) extentsList.item(i);
            if (ext_elem.hasAttribute("class_name")) {
                try {
                    Class extent_class = Class.forName(ext_elem.getAttribute("class_name"));
                    Class param_types[] = { org.w3c.dom.Document.class, org.w3c.dom.Element.class };
                    Constructor constructor = extent_class.getConstructor(param_types);
                    Object params[] = { doc, ext_elem };
                    Extent extent = (Extent) constructor.newInstance(params);
                    this.add(extent);
                } catch (Exception cnfe) {
                    this.add(new Extent(doc, (Element) extentsList.item(i)));
                }
            } else {
                this.add(new Extent(doc, (Element) extentsList.item(i)));
            }
        }
    }
