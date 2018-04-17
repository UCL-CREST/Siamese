    private static <T extends Node> T newLogicNodeInstance(Class<T> nodeType, Boolean a, Boolean b) throws Exception {
        Constructor<T> c = nodeType.getConstructor(Node.class, Node.class);
        return c.newInstance(new Const(a), new Const(b));
    }
