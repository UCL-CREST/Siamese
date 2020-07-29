    protected DocumentNotifier createNotifier(String className) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Class notifierClass = Class.forName(className);
        Constructor constructor = notifierClass.getConstructor(new Class[] { Properties.class });
        return (DocumentNotifier) constructor.newInstance(new Object[] { cfg });
    }
