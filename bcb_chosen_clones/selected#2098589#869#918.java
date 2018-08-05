    private void showDefaultPanel(String devName) {
        Class atkpanelCl;
        Constructor atkpanelClNew;
        Class[] atkpanelParamCls = new Class[5];
        Object[] params = new Object[5];
        System.out.println("showDefaultPanel called");
        try {
            atkpanelCl = Class.forName("atkpanel.MainPanel");
        } catch (ClassNotFoundException clex) {
            showErrorMsg("showDefaultPanel : atkpanel.MainPanel not found; ignored.\n");
            return;
        }
        try {
            atkpanelParamCls[0] = devName.getClass();
            atkpanelParamCls[1] = Class.forName("java.lang.Boolean");
            atkpanelParamCls[2] = atkpanelParamCls[1];
            atkpanelParamCls[3] = atkpanelParamCls[1];
            atkpanelParamCls[4] = atkpanelParamCls[1];
            atkpanelClNew = atkpanelCl.getConstructor(atkpanelParamCls);
        } catch (ClassNotFoundException clex) {
            showErrorMsg("showDefaultPanel :java.lang.Boolean not found; ignored.\n");
            return;
        } catch (Exception e) {
            showErrorMsg("showDefaultPanel : Failed to load the constructor (five arguments) for atkpanel read-only.\n");
            return;
        }
        params[0] = devName;
        params[1] = Boolean.FALSE;
        params[2] = Boolean.TRUE;
        params[3] = Boolean.FALSE;
        params[4] = Boolean.TRUE;
        try {
            Object obj = atkpanelClNew.newInstance(params);
            PanelItem newPanel = addNewPanel(obj, "atkpanel.MainPanel", devName);
            if (newPanel != null) showPanelWindow(newPanel.parent);
        } catch (InstantiationException instex) {
            showErrorMsg("Failed to instantiate 1 the atkpanel read-only.\n");
        } catch (IllegalAccessException accesex) {
            showErrorMsg("Failed to instantiate 2 the atkpanel read-only.\n");
        } catch (IllegalArgumentException argex) {
            showErrorMsg("Failed to instantiate 3 the atkpanel read-only.\n");
        } catch (InvocationTargetException invoqex) {
            showErrorMsg("Failed to instantiate 4 the atkpanel read-only.\n");
            System.out.println(invoqex);
            System.out.println(invoqex.getMessage());
            invoqex.printStackTrace();
        } catch (Exception e) {
            showErrorMsg("Got an exception when instantiate the default panel : atkpanel readonly.\n");
        }
    }
