    public <T> T createThunk(Class<T> proxyType, ObjectCreator objectCreator, String description) {
        Defense.notNull(proxyType, "proxyType");
        Defense.notNull(objectCreator, "objectCreator");
        Defense.notBlank(description, "description");
        if (!proxyType.isInterface()) throw new IllegalArgumentException(String.format("Thunks may only be created for interfaces; %s is a class.", ClassFabUtils.toJavaClassName(proxyType)));
        final Class thunkClass = getThunkClass(proxyType);
        Throwable failure;
        try {
            return proxyType.cast(thunkClass.getConstructors()[0].newInstance(description, objectCreator));
        } catch (InvocationTargetException ex) {
            failure = ex.getTargetException();
        } catch (Exception ex) {
            failure = ex;
        }
        throw new RuntimeException(String.format("Exception instantiating thunk class %s: %s", thunkClass.getName(), InternalUtils.toMessage(failure)), failure);
    }
