    public void attachApplication(ApplicationPluginModel model, boolean start) throws Exception {
        @SuppressWarnings("unchecked") Class<AbstractApplicationPlugin> classImpl = (Class<AbstractApplicationPlugin>) Class.forName(model.getClassName());
        Context appContext = parentRouter.getContext().createChildContext();
        appContext.getAttributes().put(ContextAttributes.SETTINGS, getSettings());
        appContext.getAttributes().put(ContextAttributes.APP_REGISTER, false);
        appContext.getAttributes().put(ContextAttributes.APP_ATTACH_REF, model.getUrlAttach());
        appContext.getAttributes().put(ContextAttributes.APP_ID, model.getId());
        appContext.getAttributes().put(ContextAttributes.APP_STORE, store);
        Class<?>[] objParam = new Class<?>[2];
        objParam[0] = Context.class;
        objParam[1] = ApplicationPluginModel.class;
        Constructor<AbstractApplicationPlugin> constructor;
        constructor = classImpl.getConstructor(objParam);
        AbstractApplicationPlugin appImpl = constructor.newInstance(appContext, model);
        getSettings().getAppRegistry().attachApplication(appImpl, start);
    }
