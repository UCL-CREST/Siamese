    @SuppressWarnings("unchecked")
    public T newGenericInstance(final int n) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (constructor == null) {
            constructor = getGenericParameterClass(n).getConstructor();
        }
        return (T) constructor.newInstance();
    }
