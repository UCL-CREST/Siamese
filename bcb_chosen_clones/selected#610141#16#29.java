    Object newInstance(String className, Class<?>[] parameterTypes, Object[] parameterValues) throws Exception {
        Class<?> targetType = Class.forName(className);
        Constructor<?> constructor = targetType.getConstructor(parameterTypes);
        boolean accessible = constructor.isAccessible();
        try {
            setAccessible(constructor, true);
            return constructor.newInstance(parameterValues);
        } finally {
            try {
                setAccessible(constructor, accessible);
            } catch (RuntimeException e) {
            }
        }
    }
