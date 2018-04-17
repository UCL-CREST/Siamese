    static Object createInternalObject(String name, String className, ApplicationContext context) throws InstantiationException {
        try {
            Class clazz = ClassUtils.forName(className);
            Constructor constructor = clazz.getConstructor(new Class[] { ApplicationContext.class, String.class });
            Object[] constructorArgs = new Object[] { context, name };
            return constructor.newInstance(constructorArgs);
        } catch (Throwable t) {
            throw new InstantiationException(name, className, t);
        }
    }
