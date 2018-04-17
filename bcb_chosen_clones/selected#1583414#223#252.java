    public static Object copyObject(Object copyObject) {
        Constructor<?> constructor;
        Class<?>[] constructorParameterArray;
        Object[] parameterArray;
        if (copyObject == null) {
            return null;
        }
        try {
            constructorParameterArray = new Class[1];
            constructorParameterArray[0] = copyObject.getClass();
            constructor = copyObject.getClass().getConstructor(constructorParameterArray);
            parameterArray = new Object[1];
            parameterArray[0] = copyObject;
            return constructor.newInstance(parameterArray);
        } catch (NoSuchMethodException ex) {
            throw new IllegalArgumentException("NoSuchMethodException: Error finding constructor to create copy:" + copyObject.getClass().getName());
        } catch (IllegalAccessException ex) {
            throw new IllegalArgumentException("IllegalAccessException: No access to run constructor to create copy" + copyObject.getClass().getName());
        } catch (InstantiationException ex) {
            throw new IllegalArgumentException("InstantiationException: Unable to instantiate constructor to copy" + copyObject.getClass().getName());
        } catch (java.lang.reflect.InvocationTargetException ex) {
            if (ex.getCause() instanceof Error) {
                throw (Error) ex.getCause();
            } else if (ex.getCause() instanceof RuntimeException) {
                throw (RuntimeException) ex.getCause();
            } else {
                throw new IllegalArgumentException("InvocationTargetException: Unable to invoke constructor to create copy");
            }
        }
    }
