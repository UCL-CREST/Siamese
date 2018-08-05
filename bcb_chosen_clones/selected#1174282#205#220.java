    public AddonInterface getAddon(final String name) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
        Class<?> PluginClass;
        try {
            PluginClass = pluginLoader.loadClass("net.sourceforge.jepesi.addon." + name.toLowerCase() + "." + name);
        } catch (Exception e) {
            PluginClass = pluginLoader.loadPlugin("net.sourceforge.jepesi.addon." + name.toLowerCase(), AddonDir + name + ".jar", name);
        }
        Object[] aoParams = new Object[1];
        aoParams[0] = jepseiControl;
        Class<?>[] acParams = new Class[1];
        acParams[0] = JepesiInterface.class;
        Constructor<?> oConstr;
        oConstr = PluginClass.getConstructor(acParams);
        AddonInterface pli = (AddonInterface) oConstr.newInstance(aoParams);
        return pli;
    }
