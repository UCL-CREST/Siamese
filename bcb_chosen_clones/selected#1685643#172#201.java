    @SuppressWarnings("unchecked")
    private static URL getPluginImageURL(Object plugin, String name) throws Exception {
        try {
            Class bundleClass = Class.forName("org.osgi.framework.Bundle");
            Class bundleContextClass = Class.forName("org.osgi.framework.BundleContext");
            if (bundleContextClass.isAssignableFrom(plugin.getClass())) {
                Method getBundleMethod = bundleContextClass.getMethod("getBundle", new Class[] {});
                Object bundle = getBundleMethod.invoke(plugin, new Object[] {});
                Class ipathClass = Class.forName("org.eclipse.core.runtime.IPath");
                Class pathClass = Class.forName("org.eclipse.core.runtime.Path");
                Constructor pathConstructor = pathClass.getConstructor(new Class[] { String.class });
                Object path = pathConstructor.newInstance(new Object[] { name });
                Class platformClass = Class.forName("org.eclipse.core.runtime.Platform");
                Method findMethod = platformClass.getMethod("find", new Class[] { bundleClass, ipathClass });
                return (URL) findMethod.invoke(null, new Object[] { bundle, path });
            }
        } catch (Throwable e) {
            LOG.debug("Ignore any exceptions");
        }
        Class pluginClass = Class.forName("org.eclipse.core.runtime.Plugin");
        if (pluginClass.isAssignableFrom(plugin.getClass())) {
            Class ipathClass = Class.forName("org.eclipse.core.runtime.IPath");
            Class pathClass = Class.forName("org.eclipse.core.runtime.Path");
            Constructor pathConstructor = pathClass.getConstructor(new Class[] { String.class });
            Object path = pathConstructor.newInstance(new Object[] { name });
            Method findMethod = pluginClass.getMethod("find", new Class[] { ipathClass });
            return (URL) findMethod.invoke(plugin, new Object[] { path });
        }
        return null;
    }
