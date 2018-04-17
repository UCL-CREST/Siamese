    private boolean checkForGrandChildContext(Class objectClass) throws Exception {
        LOG.trace("Checking for grandchild context");
        Constructor constructor = objectClass.getConstructor((Class[]) null);
        HxTiObject object = (HxTiObject) constructor.newInstance((Object[]) null);
        if (!object.hasParentContext()) return false;
        return object.getParentObject().hasParentContext();
    }
