    public AbstractEvent createEvent(Class<? extends AbstractEvent> theClass, TestImpl theTest, Event theInitialConfig) throws ConfigurationException {
        Class<? extends Event> configType = myEventClasses2ConfigTypes.get(theClass);
        try {
            Constructor<? extends AbstractEvent> constructor = theClass.getConstructor(TestImpl.class, configType);
            final Event convertConfig = convertConfig(theInitialConfig, configType);
            AbstractEvent event = constructor.newInstance(theTest, convertConfig);
            return event;
        } catch (InstantiationException ex) {
            throw new ConfigurationException(ex.getMessage(), ex);
        } catch (IllegalAccessException ex) {
            throw new ConfigurationException(ex.getMessage(), ex);
        } catch (IllegalArgumentException ex) {
            throw new ConfigurationException(ex.getMessage(), ex);
        } catch (InvocationTargetException ex) {
            throw new ConfigurationException(ex.getMessage(), ex);
        } catch (NoSuchMethodException ex) {
            throw new ConfigurationException(ex.getMessage(), ex);
        } catch (SecurityException ex) {
            throw new ConfigurationException(ex.getMessage(), ex);
        }
    }
