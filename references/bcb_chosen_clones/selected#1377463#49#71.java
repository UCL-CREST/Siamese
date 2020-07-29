    public void initPlugins() {
        List<Class<? extends ILTPLugin>> pluginClasses = new PluginLoader(ltSetting, messanger).getPluginClasses((LTCLassLoader) applicationContext.getClassLoader());
        plugins = new ArrayList<ILTPLugin>();
        for (Class<? extends ILTPLugin> class1 : pluginClasses) {
            try {
                Constructor<ILTPLugin> con = (Constructor<ILTPLugin>) class1.getConstructor();
                ILTPLugin plugin = con.newInstance();
                if (isIDPluginAlreadyUsed(plugin)) {
                    String msg = "Plugin with ID=" + plugin.getPluginID() + " has been loaded already. Plugin won't be loaded. Plugin name:" + plugin.getName();
                    logger.error(msg);
                    messanger.addMessage(msg);
                    continue;
                }
                plugin.setServerFacade(pluginFacade);
                plugins.add(plugin);
            } catch (Exception e) {
                logger.fatal("Cannot make instance from " + class1);
            }
        }
        for (ILTPLugin plugin : plugins) {
            plugin.serverStarting();
        }
    }
