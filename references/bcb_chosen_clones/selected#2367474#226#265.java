    private Object getSingleArgumentConstructorValue(ClassLoader classLoader, Class clazz, String argument) throws ModelException {
        if (argument.startsWith("${")) {
            if (argument.endsWith("}")) {
                final String key = argument.substring(2, argument.length() - 1);
                Object value = m_map.get(key);
                if (value != null) {
                    if (value instanceof EntryModel) {
                        return ((EntryModel) value).getValue();
                    } else {
                        return value;
                    }
                } else {
                    final String error = "Unresolvable primative context value: '" + key + "'.";
                    throw new ModelException(error);
                }
            } else {
                final String error = "Illegal format for context reference: '" + argument + "'.";
                throw new ModelException(error);
            }
        } else {
            try {
                final Class[] params = new Class[] { String.class };
                Constructor constructor = clazz.getConstructor(params);
                final Object[] values = new Object[] { argument };
                return constructor.newInstance(values);
            } catch (NoSuchMethodException e) {
                final String error = "Class: '" + clazz.getName() + "' does not implement a single string argument constructor.";
                throw new ModelException(error);
            } catch (InstantiationException e) {
                final String error = "Unable to instantiate instance of class: " + clazz.getName() + " with the single argument: '" + argument + "'";
                throw new ModelException(error, e);
            } catch (IllegalAccessException e) {
                final String error = "Cannot access single string parameter constructor for the class: '" + clazz.getName() + "'.";
                throw new ModelException(error, e);
            } catch (Throwable e) {
                final String error = "Unexpected exception while creating a single string parameter value for the class: '" + clazz.getName() + "'.";
                throw new ModelException(error, e);
            }
        }
    }
