    private AppNode getAppNode(AppContext app) {
        AppDescriptor appDescriptor = app.getDescriptor();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ImageIcon icon = null;
        try {
            String pkg = Tools.getPackage(appDescriptor.getClassName());
            URL url = classLoader.getResource(pkg + "/icon.png");
            if (url == null) url = classLoader.getResource("icon.png");
            icon = new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        } catch (Exception ex) {
            Tools.logException(OptionsPanelManager.class, ex, "Could not load icon " + " for app " + appDescriptor.getClassName());
        }
        if (!appDescriptor.isHME()) {
            AppConfigurationPanel appConfigurationPanel = null;
            try {
                Class configurationPanel = classLoader.loadClass(appDescriptor.getConfigurationPanel());
                Class[] parameters = new Class[1];
                parameters[0] = AppConfiguration.class;
                Constructor constructor = configurationPanel.getConstructor(parameters);
                AppConfiguration[] values = new AppConfiguration[1];
                values[0] = (AppConfiguration) app.getConfiguration();
                appConfigurationPanel = (AppConfigurationPanel) constructor.newInstance((Object[]) values);
            } catch (Exception ex) {
                ex.printStackTrace();
                Tools.logException(OptionsPanelManager.class, ex, "Could not load configuration panel " + appDescriptor.getConfigurationPanel() + " for app " + appDescriptor.getClassName());
            }
            AppNode appNode = new AppNode(app, icon, appConfigurationPanel);
            return appNode;
        } else {
            return new AppNode(app, icon, new HMEConfigurationPanel(app.getConfiguration()));
        }
    }
