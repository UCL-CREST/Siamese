    public <T extends AbstractEvent, V extends AbstractEventEditorForm> V createEditorForm(EventEditorContextController theController, T event) throws ConfigurationException {
        Class<V> formClass = (Class<V>) myEventClasses2EditorForm.get(event.getClass());
        if (formClass == null) {
            throw new ConfigurationException("No editor for " + event.getClass());
        }
        try {
            Constructor<V> constructor = formClass.getConstructor();
            V instance = constructor.newInstance();
            instance.setController(theController);
            return instance;
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
