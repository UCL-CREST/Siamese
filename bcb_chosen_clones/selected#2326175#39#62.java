    @SuppressWarnings("unchecked")
    public void run(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new ZLOwnXMLProcessorFactory();
        new ZLOwnHtmlProcessorFactory();
        loadProperties();
        new ZLXMLConfigManager(System.getProperty("user.home") + "/." + getInstance().getApplicationName());
        new ZLSwingImageManager();
        new ZLSwingDialogManager();
        ZLApplication application = null;
        try {
            application = (ZLApplication) getApplicationClass().getConstructor(String[].class).newInstance(new Object[] { args });
        } catch (Exception e) {
            e.printStackTrace();
            shutdown();
        }
        ZLSwingApplicationWindow mainWindow = ((ZLSwingDialogManager) ZLSwingDialogManager.getInstance()).createApplicationWindow(application);
        application.initWindow();
        mainWindow.run();
    }
