    @SuppressWarnings("unchecked")
    public T newGenericInstance(final int n, final Object... objects) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Type type = types[n];
        Class<?> rawType = type instanceof Class<?> ? (Class<?>) type : (Class<?>) ((ParameterizedType) type).getRawType();
        Class<?>[] types = new Class[objects.length];
        for (int i = 0; i < objects.length; i++) {
            types[i] = objects[i].getClass();
        }
        constructor = rawType.getConstructor(types);
        return (T) constructor.newInstance(objects);
    }
