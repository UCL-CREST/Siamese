    private ingenias.editor.entities.RoleEntity restoreRole(ObjectManager om, RelationshipManager rm, GraphManager gm, Node n) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        String type = n.getAttributes().getNamedItem("type").getNodeValue();
        Class encClass = Class.forName(type);
        int index = type.lastIndexOf(".");
        String className = type.substring(index + 1, type.length());
        Class objectManager = om.getClass();
        Object[] params = { "" };
        Class[] paramtype = { "".getClass() };
        Constructor c = encClass.getConstructor(paramtype);
        RoleEntity en = (RoleEntity) c.newInstance(params);
        NodeList children = n.getChildNodes();
        for (int k = 0; k < children.getLength(); k++) {
            Node current = children.item(k);
            PersistenceManager.getPL().restoreProperty(om, gm, current, en);
        }
        return en;
    }
