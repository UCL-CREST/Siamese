    public Object newInstanceNoConstruction(final Class objectClass) throws ReflectException {
        Constructor constr;
        try {
            constr = reflectionFactory.newConstructorForSerialization(objectClass, Object.class.getConstructor(new Class[0]));
            return constr.newInstance();
        } catch (SecurityException exception) {
            throw new ReflectException(FAILED_CREATE_INSTANCE_WITHOUT_CONSTRUCTOR_INVOCATION, exception);
        } catch (NoSuchMethodException exception) {
            throw new ReflectException(FAILED_CREATE_INSTANCE_WITHOUT_CONSTRUCTOR_INVOCATION, exception);
        } catch (IllegalArgumentException exception) {
            throw new ReflectException(FAILED_CREATE_INSTANCE_WITHOUT_CONSTRUCTOR_INVOCATION, exception);
        } catch (InstantiationException exception) {
            throw new ReflectException(FAILED_CREATE_INSTANCE_WITHOUT_CONSTRUCTOR_INVOCATION, exception);
        } catch (IllegalAccessException exception) {
            throw new ReflectException(FAILED_CREATE_INSTANCE_WITHOUT_CONSTRUCTOR_INVOCATION, exception);
        } catch (InvocationTargetException exception) {
            throw new ReflectException(FAILED_CREATE_INSTANCE_WITHOUT_CONSTRUCTOR_INVOCATION, exception);
        }
    }
