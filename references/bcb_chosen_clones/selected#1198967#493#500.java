    private static <T extends Node> T newLogicNodeInstance(Class<T> nodeType, Boolean a, Boolean b, Boolean c) throws Exception {
        Constructor<T> constructor = nodeType.getConstructor(Collection.class);
        ArrayList<Node> children = new ArrayList<Node>(3);
        children.add(new Const(a));
        children.add(new Const(b));
        children.add(new Const(c));
        return constructor.newInstance(children);
    }
