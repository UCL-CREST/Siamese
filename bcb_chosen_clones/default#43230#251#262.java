    public static void invokeMain(Class<?> clazz, String[] args) throws NoSuchMethodException, InvocationTargetException {
        Class<?>[] params = new Class[] { String[].class };
        Method main = clazz.getMethod("main", params);
        if (!Modifier.isStatic(main.getModifiers())) {
            throw new NoSuchMethodException("main method in " + clazz + " not static!");
        }
        try {
            main.invoke(null, new Object[] { args });
        } catch (IllegalAccessException e) {
            throw new NoSuchMethodException("main method in " + clazz + " not accessible!");
        }
    }
