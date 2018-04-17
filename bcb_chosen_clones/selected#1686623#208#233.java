    public boolean fromSaiphXML(Node node) throws DOMException, InvalidMidiDataException, ClassNotFoundException, SecurityException, NoSuchMethodException, NumberFormatException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
        boolean ok = true;
        setName(((Element) node).getAttribute("name"));
        setInfo(((Element) node).getAttribute("info"));
        Node limitNode = ((Element) node).getElementsByTagName("limit").item(0);
        String t = ((Element) limitNode).getAttribute("type");
        String l = limitNode.getFirstChild().getNodeValue();
        int ix = Integer.parseInt(t);
        ok = (ix >= 0 && ix <= 9);
        if (!ok) return false;
        long lx = Long.parseLong(l);
        ok = (lx > 1 && lx <= Long.MAX_VALUE);
        if (!ok) return false;
        setLimit(ix, lx);
        Node dgNode = ((Element) node).getElementsByTagName("durationGenerator").item(0);
        Node vgNode = ((Element) dgNode).getFirstChild();
        String typeAttr = ((Element) vgNode).getAttribute("type");
        String dgClassName = ((Element) vgNode).getAttribute("class");
        Class dgClass = Class.forName(dgClassName);
        Constructor dgClassConstructor = dgClass.getConstructor(new Class[] { int.class });
        Object dg = dgClassConstructor.newInstance(new Object[] { Integer.valueOf(typeAttr) });
        ok = ((SaiphXML) dg).fromSaiphXML(vgNode);
        if (!ok) return false;
        setDurationGenerator((ValuesGenerator) dg);
        return ok;
    }
