    @SuppressWarnings("unchecked")
    public static Object classForName(final String className, final Object[] args) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        final Class c = Class.forName(className);
        final Constructor constructors[] = c.getConstructors();
        for (int i = 0; i < constructors.length; i++) {
            final Constructor constructor = constructors[i];
            final Class types[] = constructor.getParameterTypes();
            if (args == null) {
                return constructor.newInstance();
            } else if (types.length == args.length) {
                return constructor.newInstance(args);
            }
        }
        return null;
    }
