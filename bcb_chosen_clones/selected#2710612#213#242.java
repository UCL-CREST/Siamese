    private static Model getModelInstance(Class<?> c, String className, Object... parameters) throws WrongConfigurationException {
        Class<?>[] types = new Class[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            types[1] = parameters[i].getClass();
        }
        Model result = null;
        try {
            Constructor<?> constructor = c.getConstructor(types);
            result = (Model) constructor.newInstance(parameters);
        } catch (SecurityException e) {
            throw new WrongConfigurationException("Cannot generate constructor of the model '" + className + "' due to a SecurityException: " + e.getMessage());
        } catch (NoSuchMethodException e) {
            String paramTypes = "";
            for (Class<?> cl : types) {
                paramTypes += cl.getSimpleName() + ", ";
            }
            throw new WrongConfigurationException(e, "Cannot generate constructor of the model '" + className + "'. There is no constructor that takes the parameters: (" + paramTypes + ").");
        } catch (IllegalArgumentException e) {
            throw new WrongConfigurationException(e, "Cannot generate instance of the model '" + className + "' due to wrong arguments.");
        } catch (InstantiationException e) {
            String cause = e.getCause() == null ? "" : e.getCause().getMessage();
            throw new WrongConfigurationException(e, "Cannot generate instance of the model '" + className + "' : " + cause);
        } catch (IllegalAccessException e) {
            throw new WrongConfigurationException(e, "Cannot generate instance of the model '" + className + "'");
        } catch (InvocationTargetException e) {
            String cause = e.getCause() == null ? "" : e.getCause().getMessage();
            throw new WrongConfigurationException(e, "Cannot generate instance of the model '" + className + "' :  " + cause);
        }
        return result;
    }
