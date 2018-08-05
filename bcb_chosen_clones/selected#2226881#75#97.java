    @SuppressWarnings("unchecked")
    public Object createInstance(PicoContainer container) throws ConfigurationException {
        List<Object> constructionValues = new ArrayList<Object>();
        List<Class> constructionTypes = new ArrayList<Class>();
        for (ConstructorArgument argument : _constructorArguments) {
            Object value = argument.getValueProvider().getValue(container);
            if (value == null) {
                throw new ConfigurationException("Cannot pass null value to constructor.");
            }
            constructionValues.add(value);
            constructionTypes.add(value.getClass());
        }
        try {
            Constructor constructor = _type.getConstructor(constructionTypes.toArray(new Class[constructionTypes.size()]));
            IPropertyContainer result = (IPropertyContainer) constructor.newInstance(constructionValues.toArray());
            for (PropertyInitializer initializer : _propertyInitializers.values()) {
                initializer.initializeObjectProperty(result, container);
            }
            return result;
        } catch (Exception e) {
            throw new ConfigurationException(e.getMessage(), e);
        }
    }
