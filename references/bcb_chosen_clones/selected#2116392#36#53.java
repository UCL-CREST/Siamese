    public static Object silentlyCreate(String className, Object[] constructorArgs) {
        try {
            Class clazz = Class.forName(className);
            Constructor[] constructors = clazz.getConstructors();
            for (int i = 0; i < constructors.length; i++) {
                if (constructors[i].getParameterTypes().length == constructorArgs.length) {
                    return constructors[i].newInstance(constructorArgs);
                }
            }
        } catch (Exception e) {
            if (logger != null) {
                logger.error("Failed to create a " + className + " instance", e);
            } else {
                console.error("Failed to create a " + className + " instance", e);
            }
        }
        return null;
    }
