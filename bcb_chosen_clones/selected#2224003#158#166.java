    @SuppressWarnings("unchecked")
    private static AbstractATHandler load(CSerialDriver serialDriver, Logger log, CService srv, String handlerClassName) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, InvocationTargetException, IllegalAccessException {
        log.info("Attempting to load handler: " + handlerClassName);
        Class<AbstractATHandler> handlerClass = (Class<AbstractATHandler>) Class.forName(handlerClassName);
        java.lang.reflect.Constructor<AbstractATHandler> handlerConstructor = handlerClass.getConstructor(new Class[] { CSerialDriver.class, Logger.class, CService.class });
        AbstractATHandler atHandlerInstance = handlerConstructor.newInstance(new Object[] { serialDriver, log, srv });
        log.info("Successfully loaded handler: " + atHandlerInstance.getClass().getName());
        return atHandlerInstance;
    }
