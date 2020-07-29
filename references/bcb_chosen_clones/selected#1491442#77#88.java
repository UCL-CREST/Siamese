    public Object wrapAs(Object entity, Class<?> loadingInterface) {
        if (entity == null) {
            return null;
        }
        try {
            Class<?> wrapperClass = getWrapper(loadingInterface);
            java.lang.reflect.Constructor<?> constructor = wrapperClass.getConstructor(entity.getClass());
            return constructor.newInstance(entity);
        } catch (Exception e) {
            throw new RuntimeException("Wrapper creation exception", e);
        }
    }
