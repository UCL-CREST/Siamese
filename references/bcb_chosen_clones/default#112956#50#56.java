    private static RenderCanvas _create(String className, int x, int y) throws Exception {
        Class clazz = Class.forName(className);
        Constructor constructor = clazz.getConstructor(new Class[] { int.class, int.class });
        Object[] params = new Object[] { new Integer(x), new Integer(y) };
        RenderCanvas result = (RenderCanvas) constructor.newInstance(params);
        return result;
    }
