    private void addTavernaMenu() {
        if (!Boolean.parseBoolean(Config.config.getProperty("Taverna.Enable"))) {
            return;
        }
        String value = Config.language.getProperty("Menu.Taverna");
        if (value == null) {
            throw new RuntimeException("Key \"Menu.Taverna\" is missing from the language file");
        }
        JMenu tavernaMenu = new JMenu(value);
        this.add(tavernaMenu);
        tavernaMenu.setMnemonic('v');
        Frame parentFrame = this.getParentFrame();
        Class<TavernaApi> clazz;
        try {
            URLClassLoader ucl = OVTK2PluginLoader.getInstance().ucl;
            Thread.currentThread().setContextClassLoader(ucl);
            String classname = "net.sourceforge.ondex.taverna.TavernaWrapper";
            clazz = (Class<TavernaApi>) ucl.loadClass(classname);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("No TavernaWrapper found. ");
            System.err.println(e.getMessage());
            JMenuItem missing = makeMenuItem("Menu.Taverna.Missing", "TavernaMissing");
            NoTavernaMenuAction helpListener = new NoTavernaMenuAction();
            desktop.addActionListener(helpListener);
            tavernaMenu.add(missing);
            return;
        }
        try {
            Constructor<TavernaApi> constructor = clazz.getConstructor(parentFrame.getClass());
            TavernaApi travernaApi = constructor.newInstance(parentFrame);
            travernaApi.attachMenu(tavernaMenu);
            travernaApi.setTavernaHome(Config.config.getProperty("Taverna.TravenaHome"));
            travernaApi.setDataViewerHome(Config.config.getProperty("Taverna.DataViewerHomer"));
            File dataDir = new File(net.sourceforge.ondex.config.Config.ondexDir);
            travernaApi.setRootDirectory(dataDir);
            Icon icon = new ImageIcon("config/toolbarButtonGraphics/taverna/taverna.jpeg");
        } catch (Exception e) {
            ErrorDialog.show(e);
        }
    }
