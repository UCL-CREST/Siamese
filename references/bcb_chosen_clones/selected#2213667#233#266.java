    private static Parameter initParameter(final Element paramElement, final LiveSeed liveSeed) throws PersistenceManagerException {
        try {
            final String name = paramElement.getAttribute(TYPE_ATT);
            final Class parameterClass = Class.forName(name);
            final String directionParamString = paramElement.getAttribute(DIRECTION_ATT);
            final String numericParamString = paramElement.getAttribute(NUMERIC_PARAM_ATT);
            if (AbstractResourceParameter.class.isAssignableFrom(parameterClass)) {
                final Direction direction;
                if (directionParamString != null && directionParamString.length() > 0) {
                    direction = getDirection(directionParamString);
                } else {
                    direction = null;
                }
                return (Parameter) parameterClass.getConstructor(new Class[] { Direction.class }).newInstance(new Object[] { direction });
            } else if (ResourceParameter.class.isAssignableFrom(parameterClass)) {
                return (Parameter) parameterClass.getConstructor(new Class[] {}).newInstance(new Object[] {});
            } else if (ConstantParameter.class.isAssignableFrom(parameterClass)) {
                final Integer numericValue = new Integer(numericParamString);
                return (Parameter) parameterClass.getConstructor(new Class[] { int.class }).newInstance(new Object[] { numericValue });
            } else {
                throw new PersistenceManagerException(MessageFormat.format(RESOURCE_BUNDLE.getString("exception.unknownParam"), new Object[] { name }));
            }
        } catch (ClassNotFoundException e) {
            throw new PersistenceManagerException(e);
        } catch (NoSuchMethodException e) {
            throw new PersistenceManagerException(e);
        } catch (IllegalAccessException e) {
            throw new PersistenceManagerException(e);
        } catch (InvocationTargetException e) {
            throw new PersistenceManagerException(e);
        } catch (InstantiationException e) {
            throw new PersistenceManagerException(e);
        }
    }
