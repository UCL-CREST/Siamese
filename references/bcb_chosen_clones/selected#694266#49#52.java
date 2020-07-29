    public T createHolder(URL url) throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Constructor<T> ctor = cls.getConstructor(URL.class);
        return ctor.newInstance(url);
    }
