    public static Connector load(Unit aUnit, Element aElement) {
        String theType = aElement.getLocalName();
        Class theClass;
        if ("simpleConnector".equals(theType)) theClass = SimpleConnector.class; else if ("toStringConnector".equals(theType)) theClass = ToStringConnector.class; else throw new RuntimeException("Not handled: " + theType);
        try {
            Constructor theConstructor = theClass.getConstructor(Slot.class, Slot.class);
            Connector theConnector = (Connector) theConstructor.newInstance(findSlot(aUnit, aElement.getAttributeValue("src")), findSlot(aUnit, aElement.getAttributeValue("dst")));
            theConnector.finishLoading(aElement);
            return theConnector;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
