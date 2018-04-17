    public FileAdapterAttributeValue(XACMLElement engineElem) throws Exception {
        this.engineElem = engineElem;
        AttributeValue attrVal = (AttributeValue) engineElem;
        if (this.engineElem.getElementName() == null) {
            this.engineElem.setElementName(ELEMENT_NAME);
        }
        xmlElement = createPolicyElement();
        xmlElement.setAttribute(ATTR_DATATYPE, attrVal.getDataType().toString());
        Expression childExp = attrVal.getChildExpression();
        if (childExp != null) {
            Class<?> dataAdapterClz = getPolicyDataAdapterClassByXACMLElementType(childExp.getClass());
            Constructor<?> daConstr = dataAdapterClz.getConstructor(XACMLElement.class);
            DataAdapter da = (DataAdapter) daConstr.newInstance(childExp);
            xmlElement.appendChild((Element) da.getDataStoreObject());
        } else {
            xmlElement.appendChild(getDefaultDocument().createTextNode(attrVal.getValue().toString()));
        }
    }
