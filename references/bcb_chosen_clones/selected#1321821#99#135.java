    private IAXLangElement fromJDOM(Element theJDOMElement, Model theModel, IAXLangElement theParent) {
        IAXLangElement axlReturn = null;
        Model axlModel = theModel;
        try {
            ReferenceKind theKind = ReferenceKind.valueOf(theJDOMElement.getAttributeValue(Attributes.KIND.getID()).toUpperCase());
            Role theRole = Role.valueOf(theJDOMElement.getAttributeValue(Attributes.ROLE.getID()).toUpperCase());
            Class<? extends IAXLangElement> clsElement = theRole.getType();
            Constructor<?> theConstructor = null;
            try {
                theConstructor = clsElement.getConstructor();
                axlReturn = (Model) theConstructor.newInstance();
                axlModel = (Model) axlReturn;
            } catch (NoSuchMethodException e) {
                try {
                    theConstructor = clsElement.getConstructor(Model.class, IAXLangElement.class);
                    axlReturn = (IAXLangElement) theConstructor.newInstance(axlModel, theParent);
                } catch (NoSuchMethodException e1) {
                }
            }
            if (axlReturn == null) {
                throw new AXLException("Element could not be constructed from XML.");
            }
            if (theParent != null) {
                theParent.addElement(axlReturn, theKind, theRole);
            }
            for (Element theAttributeElement : (List<Element>) theJDOMElement.getChildren(AdditionalXML.ATTRIBUTE.getXML())) {
                axlReturn.addAttribute(new AXLangAttribute(theAttributeElement.getAttributeValue(Attributes.IDENTIFIER.getID()), theAttributeElement.getTextTrim(), Boolean.parseBoolean(theAttributeElement.getAttributeValue(Attributes.ISOPTIONAL.getID())), Boolean.parseBoolean(theAttributeElement.getAttributeValue(Attributes.ISDELETEABLE.getID()))));
            }
            for (Element theElement : (List<Element>) theJDOMElement.getChildren(AdditionalXML.ELEMENT.getXML())) {
                fromJDOM(theElement, axlModel, axlReturn);
            }
        } catch (Exception e) {
            addAXLMessage(e.getMessage(), MessageType.ERROR);
            e.printStackTrace();
        }
        return axlReturn;
    }
