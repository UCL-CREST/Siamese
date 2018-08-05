    void openDesktop() {
        if (desktop != null) {
            System.out.println("openDesktop: desktop already open");
            return;
        }
        desktop = new Desktop();
        desktop.start();
        DefaultSwingBundleDisplayer disp;
        ServiceRegistration reg;
        String[] dispClassNames = new String[] { LargeIconsDisplayer.class.getName(), GraphDisplayer.class.getName(), TableDisplayer.class.getName(), ManifestHTMLDisplayer.class.getName(), ClosureHTMLDisplayer.class.getName(), ServiceHTMLDisplayer.class.getName(), PackageHTMLDisplayer.class.getName(), LogDisplayer.class.getName(), EventDisplayer.class.getName(), PrefsDisplayer.class.getName() };
        String dispsS = Util.getProperty("org.knopflerfish.desktop.displays", "").trim();
        if (dispsS != null && dispsS.length() > 0) {
            dispClassNames = Text.splitwords(dispsS, "\n\t ", '\"');
        }
        for (int i = 0; i < dispClassNames.length; i++) {
            String className = dispClassNames[i];
            try {
                Class clazz = Class.forName(className);
                Constructor cons = clazz.getConstructor(new Class[] { BundleContext.class });
                disp = (DefaultSwingBundleDisplayer) cons.newInstance(new Object[] { getTargetBC() });
                disp.open();
                reg = disp.register();
                displayers.put(disp, reg);
            } catch (Exception e) {
                log.warn("Failed to create displayer " + className, e);
            }
        }
        String defDisp = Util.getProperty("org.knopflerfish.desktop.display.main", LargeIconsDisplayer.NAME);
        desktop.bundlePanelShowTab(defDisp);
        int ix = desktop.detailPanel.indexOfTab("Manifest");
        if (ix != -1) {
            desktop.detailPanel.setSelectedIndex(ix);
        }
    }
