    private boolean checkForChildContext(Class objectClass) throws Exception {
        LOG.trace("Checking for child context");
        Constructor constructor = objectClass.getConstructor((Class[]) null);
        HxTiObject object = (HxTiObject) constructor.newInstance((Object[]) null);
        return object.hasParentContext();
    }
