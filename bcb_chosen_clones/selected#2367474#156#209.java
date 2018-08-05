    private Object getMultiArgumentConstructorValue(ClassLoader classLoader, Class clazz, Parameter[] parameters) throws ModelException {
        if (parameters.length == 0) {
            try {
                return clazz.newInstance();
            } catch (InstantiationException e) {
                final String error = "Unable to instantiate instance of class: " + clazz.getName();
                throw new ModelException(error, e);
            } catch (IllegalAccessException e) {
                final String error = "Cannot access null constructor for the class: '" + clazz.getName() + "'.";
                throw new ModelException(error, e);
            }
        } else {
            Class[] params = new Class[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                String classname = parameters[i].getClassname();
                try {
                    params[i] = classLoader.loadClass(classname);
                } catch (Throwable e) {
                    final String error = "Unable to resolve sub-parameter class: " + classname + " for the parameter " + clazz.getName();
                    throw new ModelException(error, e);
                }
            }
            Object[] values = new Object[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                Parameter p = parameters[i];
                String classname = p.getClassname();
                try {
                    values[i] = getValue(p);
                } catch (Throwable e) {
                    final String error = "Unable to instantiate sub-parameter for value: " + classname + " inside the parameter " + clazz.getName();
                    throw new ModelException(error, e);
                }
            }
            Constructor constructor = null;
            try {
                constructor = clazz.getConstructor(params);
            } catch (NoSuchMethodException e) {
                final String error = "Supplied parameters for " + clazz.getName() + " do not match the available class constructors.";
                throw new ModelException(error, e);
            }
            try {
                return constructor.newInstance(values);
            } catch (InstantiationException e) {
                final String error = "Unable to instantiate an instance of a multi-parameter constructor for class: '" + clazz.getName() + "'.";
                throw new ModelException(error, e);
            } catch (IllegalAccessException e) {
                final String error = "Cannot access multi-parameter constructor for the class: '" + clazz.getName() + "'.";
                throw new ModelException(error, e);
            } catch (Throwable e) {
                final String error = "Unexpected error while attmpting to instantiate a multi-parameter constructor " + "for the class: '" + clazz.getName() + "'.";
                throw new ModelException(error, e);
            }
        }
    }
