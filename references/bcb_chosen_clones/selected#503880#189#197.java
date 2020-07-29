    public static Object invokeConstructor(Class clazz, Class[] argTypes, Object[] args) {
        Constructor constructor = ReflectionUtil.getConstructor(clazz, argTypes);
        try {
            Object newObject = constructor.newInstance(args);
            return newObject;
        } catch (Exception e) {
            throw new YamlException("Can't invoke constructor for " + clazz + " with arguments " + Arrays.asList(argTypes) + " with values " + Arrays.asList(args));
        }
    }
