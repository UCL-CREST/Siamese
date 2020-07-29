    private void launchPanel(JDObject comp, String compName, boolean isAdevice) {
        if (comp.hasExtendedParam("className")) {
            String pname = comp.getExtendedParam("className");
            if (isNoPanel(pname)) return;
        }
        if (!comp.hasExtendedParam("className")) {
            if (isAdevice) {
                Window w = getPanel("atkpanel.MainPanel", compName);
                if (w == null) {
                    showDefaultPanel(compName);
                } else {
                    showPanelWindow(w);
                }
            }
            return;
        }
        String clName = comp.getExtendedParam("className");
        String constParam = comp.getExtendedParam("classParam");
        if (constParam.length() == 0) constParam = null;
        System.out.println("clName = " + clName + "  constParam = " + constParam + " compName = " + compName);
        Class panelCl;
        Constructor panelClNew;
        Class[] paramCls = new Class[1];
        Object[] params = new Object[1];
        if (constParam == null) {
            params[0] = compName;
        } else {
            params[0] = constParam;
        }
        System.out.println("params[0]= " + params[0]);
        Window w = getPanel(clName, (String) params[0]);
        if (w != null) {
            showPanelWindow(w);
            return;
        }
        try {
            panelCl = Class.forName(clName);
            paramCls[0] = compName.getClass();
            panelClNew = panelCl.getConstructor(paramCls);
        } catch (ClassNotFoundException clex) {
            showErrorMsg("The panel class : " + clName + " not found; ignored.\n");
            return;
        } catch (Exception e) {
            showErrorMsg("Failed to load the constructor " + clName + "( String ) for the panel class.\n");
            return;
        }
        try {
            Object obj = panelClNew.newInstance(params);
            PanelItem newPanel = addNewPanel(obj, clName, (String) params[0]);
            if (newPanel != null) showPanelWindow(newPanel.parent);
        } catch (InstantiationException instex) {
            showErrorMsg("Failed to instantiate 1 the panel class : " + clName + ".\n");
        } catch (IllegalAccessException accesex) {
            showErrorMsg("Failed to instantiate 2 the panel class : " + clName + ".\n");
        } catch (IllegalArgumentException argex) {
            showErrorMsg("Failed to instantiate 3 the panel class : " + clName + ".\n");
        } catch (InvocationTargetException invoqex) {
            showErrorMsg("Failed to instantiate 4 the panel class : " + clName + ".\n");
            System.out.println(invoqex);
            System.out.println(invoqex.getMessage());
            invoqex.printStackTrace();
        } catch (Exception e) {
            showErrorMsg("Got an exception when instantiate the panel class : " + clName + ".\n");
        }
    }
