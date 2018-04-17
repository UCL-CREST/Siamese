    private static PluginRegistry newInstance(String testClass, String registryClassName, ClassLoaderResolver clr) {
        try {
            if (clr.classForName(testClass, org.datanucleus.ClassConstants.NUCLEUS_CONTEXT_LOADER) == null) {
                if (NucleusLogger.PLUGIN.isDebugEnabled()) {
                    NucleusLogger.PLUGIN.debug(LOCALISER.msg("024005", registryClassName));
                }
            }
            return (PluginRegistry) clr.classForName(registryClassName, org.datanucleus.ClassConstants.NUCLEUS_CONTEXT_LOADER).getConstructor(new Class[] { ClassConstants.CLASS_LOADER_RESOLVER }).newInstance(new Object[] { clr });
        } catch (Exception e) {
            if (NucleusLogger.PLUGIN.isDebugEnabled()) {
                NucleusLogger.PLUGIN.debug(LOCALISER.msg("024006", registryClassName, e.getCause() != null ? e.getCause().getMessage() : e.getMessage()));
            }
        }
        return null;
    }
