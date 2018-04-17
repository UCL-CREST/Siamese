    private static URL getPluginImageURL(final Object plugin, final String name) throws Exception {
        try {
            Class<?> bundleClass = Class.forName("org.osgi.framework.Bundle");
            Class<?> bundleContextClass = Class.forName("org.osgi.framework.BundleContext");
            if (bundleContextClass.isAssignableFrom(plugin.getClass())) {
                Method getBundleMethod = bundleContextClass.getMethod("getBundle", new Class[0]);
                Object bundle = getBundleMethod.invoke(plugin, new Object[0]);
                Class<?> ipathClass = Class.forName("org.eclipse.core.runtime.IPath");
                Class<?> pathClass = Class.forName("org.eclipse.core.runtime.Path");
                Constructor<?> pathConstructor = pathClass.getConstructor(new Class[] { String.class });
                Object path = pathConstructor.newInstance(new Object[] { name });
                Class<?> platformClass = Class.forName("org.eclipse.core.runtime.Platform");
                Method findMethod = platformClass.getMethod("find", new Class[] { bundleClass, ipathClass });
                return (URL) findMethod.invoke(null, new Object[] { bundle, path });
            }
        } catch (Throwable e) {
        }
        {
            Class<?> pluginClass = Class.forName("org.eclipse.core.runtime.Plugin");
            if (pluginClass.isAssignableFrom(plugin.getClass())) {
                Class<?> ipathClass = Class.forName("org.eclipse.core.runtime.IPath");
                Class<?> pathClass = Class.forName("org.eclipse.core.runtime.Path");
                Constructor<?> pathConstructor = pathClass.getConstructor(new Class[] { String.class });
                Object path = pathConstructor.newInstance(new Object[] { name });
                Method findMethod = pluginClass.getMethod("find", new Class[] { ipathClass });
                return (URL) findMethod.invoke(plugin, new Object[] { path });
            }
        }
        return null;
    }
