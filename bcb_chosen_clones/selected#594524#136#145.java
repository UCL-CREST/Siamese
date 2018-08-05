    public static Object getConstructedObject(Class<?> clazz, Object... parameters) throws Exception {
        Class<?>[] parameterTypes = new Class[parameters.length];
        convertParameterArray(parameterTypes, parameters);
        Constructor<?> method = getConstructor(clazz, parameterTypes);
        if (method == null) {
            logMethod(Level.ERROR, clazz, clazz.getName(), parameterTypes);
            throw new NoSuchMethodError("check debug information above");
        }
        return method.newInstance(parameters);
    }
