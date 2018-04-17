    public Object create(Class[] paramTypes, Object[] args) throws NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Class c = createClass();
        Constructor cons = c.getConstructor(paramTypes);
        return cons.newInstance(args);
    }
