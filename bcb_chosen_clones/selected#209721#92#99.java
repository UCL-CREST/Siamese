    public static <T> T instantiate(final Class clazz, final Class[] parameterTypes, final Object... args) {
        try {
            final Constructor<T> constructor = clazz.getConstructor(parameterTypes);
            return constructor.newInstance(args);
        } catch (Exception e) {
            throw new IllegalStateException("Object could not instantiate for `" + clazz + "(" + parameterTypes + ")` with arguments `" + args + '`', e);
        }
    }
