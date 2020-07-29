    public Object createBeanInstance() {
        try {
            Class[] parameterTypes = getConstructor().getParameterTypes();
            Object[] parameterObjects = new Object[parameterTypes.length];
            for (int i = 0; i < parameterObjects.length; i++) {
                Class clazz = parameterTypes[i];
                parameterObjects[i] = clazz.newInstance();
            }
            return getConstructor().newInstance(parameterObjects);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
