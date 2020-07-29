    public void loadUI(String uiType) {
        Class uiClass;
        UI ui;
        try {
            uiClass = Class.forName("elcod.ui." + uiType);
            java.lang.reflect.Constructor co = uiClass.getConstructor(new Class[] { ElcodModel.class });
            ui = (UI) co.newInstance(this.elcodModel);
            ui.initUi();
        } catch (Exception ex) {
            Logger.getLogger(PluginManager.class.getName()).log(Level.SEVERE, ex.toString());
        }
    }
