    public static Object newInstance(String className, String argTypes, String args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Class<?> clazz = Class.forName(className);
        Class<?>[] ats = getArgumentTypes(argTypes);
        Object[] as = getArguments(args, ats);
        Constructor<?> constructor = clazz.getConstructor(ats);
        return constructor.newInstance(as);
    }
