    public static CRSSelectPanel getPanel(IProjection proj) {
        CRSSelectPanel panel = null;
        Class[] args = { IProjection.class };
        Object[] params = { proj };
        try {
            panel = (CRSSelectPanel) panelClass.getConstructor(args).newInstance(params);
        } catch (IllegalArgumentException e) {
            PluginServices.getLogger().error("Error creating CRS selection button", e);
        } catch (SecurityException e) {
            PluginServices.getLogger().error("Error creating CRS selection button", e);
        } catch (InstantiationException e) {
            PluginServices.getLogger().error("Error creating CRS selection button", e);
        } catch (IllegalAccessException e) {
            PluginServices.getLogger().error("Error creating CRS selection button", e);
        } catch (InvocationTargetException e) {
            PluginServices.getLogger().error("Error creating CRS selection button", e);
        } catch (NoSuchMethodException e) {
            PluginServices.getLogger().error("Error creating CRS selection button", e);
        }
        return panel;
    }
