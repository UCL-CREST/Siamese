    static URIEditorInput create(IMemento memento) {
        String bundleSymbolicName = memento.getString(BUNDLE_TAG);
        String className = memento.getString(CLASS_TAG);
        try {
            Bundle bundle = Platform.getBundle(bundleSymbolicName);
            Class<?> theClass = bundle.loadClass(className);
            Constructor<?> constructor = theClass.getConstructor(IMemento.class);
            return (URIEditorInput) constructor.newInstance(memento);
        } catch (Exception exception) {
            CommonUIPlugin.INSTANCE.log(exception);
            return new URIEditorInput(memento);
        }
    }
