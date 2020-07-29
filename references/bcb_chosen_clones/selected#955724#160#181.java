    public static ConfidenceInterval readFromXML(Element e) {
        if (e == null) return null;
        String type = e.getAttribute("type");
        if (!XMLUtils.hasValue(type)) return null;
        Class c = null;
        try {
            c = Class.forName(PACKAGE + type);
        } catch (ClassNotFoundException cnfe) {
        }
        if (c == null) try {
            c = Class.forName(type);
        } catch (ClassNotFoundException cnfe) {
            return null;
        }
        try {
            Class[] paramTypes = new Class[] { Element.class };
            Constructor cnstr = c.getConstructor(paramTypes);
            return (ConfidenceInterval) cnstr.newInstance(new Object[] { e });
        } catch (Exception ex) {
            return null;
        }
    }
