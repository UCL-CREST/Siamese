    @Override
    public UUID[] execute(ResourcePool rp) throws Exception {
        UUID[] result = super.execute(rp);
        Object[] in = getResources(rp);
        AbstractONDEXPluginInit plugin;
        AbstractONDEXPlugin<?> p = pluginClass.getConstructor(new Class<?>[] {}).newInstance(new Object[] {});
        if (in[0] != null) plugin = (AbstractONDEXPluginInit) in[0]; else plugin = new AbstractONDEXPluginInit();
        plugin.setPlugin(p);
        AbstractONDEXGraph[] graphs = new AbstractONDEXGraph[2];
        for (int i = 1; i < in.length; i++) graphs[i - 1] = (AbstractONDEXGraph) in[i];
        ONDEXGraph output = getPlugin(pluginClass).run(plugin, graphs);
        for (UUID address : result) {
            rp.addResource(address, output);
        }
        return result;
    }
