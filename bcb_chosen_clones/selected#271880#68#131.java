    public BasePlugin loadPlugin(String propertyname, Class[] classes, Object[] objects) throws Exception {
        BasePlugin plugin = null;
        log.debug("looking for classname for property " + propertyname);
        String classname = configuration.getProperty(propertyname);
        log.debug("loading plugin with name " + classname);
        try {
            plugin = (BasePlugin) Class.forName(classname).getConstructor(classes).newInstance(objects);
            log.debug("plugin loaded!");
            Enumeration keys = this.configuration.keys();
            String key;
            String value;
            String methodName;
            String pluginName;
            while (keys.hasMoreElements()) {
                key = (String) keys.nextElement();
                if (key.startsWith(propertyname + ".property")) {
                    value = configuration.getProperty(key);
                    key = key.substring(key.lastIndexOf(".") + 1);
                    log.debug(key + "/" + value);
                    methodName = "set" + key;
                    plugin.getClass().getMethod(methodName, new Class[] { String.class }).invoke(plugin, new Object[] { value });
                }
            }
            String callMethod = configuration.getProperty(propertyname + ".callMethod");
            if (callMethod != null) {
                String methods[] = callMethod.split(",");
                for (int i = 0; i < methods.length; i++) {
                    value = methods[i];
                    log.debug("callmethod " + value);
                    pluginName = "plugMail." + value.substring(0, value.lastIndexOf("."));
                    methodName = value.substring(value.lastIndexOf(".") + 1);
                    log.debug("callmethod " + pluginName + "." + methodName + "(BasePlugin)");
                    log.debug("loaded plugins: " + loadedPlugins);
                    BasePlugin targetPlugin = (BasePlugin) loadedPlugins.get(pluginName);
                    log.debug("what did we find? " + targetPlugin);
                    if (targetPlugin.acceptsPlugins()) {
                        Class clazz = plugin.getClass();
                        boolean found = false;
                        while (!found && (clazz != null)) {
                            try {
                                targetPlugin.getClass().getMethod(methodName, new Class[] { clazz }).invoke(targetPlugin, new Object[] { plugin });
                                found = true;
                            } catch (Exception e) {
                                log.debug("No method found for class " + clazz.getName());
                                clazz = clazz.getSuperclass();
                            }
                        }
                        if (!found) {
                            log.error("Tried to add a Plugin to a " + targetPlugin.getClass().getName() + " but method with name " + methodName + " not found!");
                        }
                    } else {
                        log.error("Tried to add a Plugin to a " + targetPlugin.getClass().getName());
                    }
                }
            }
            plugin.activate();
            log.debug("plugin activated!");
        } catch (Exception e) {
            log.error("Could not load plugin with name " + classname, e);
            throw new Exception(e);
        }
        loadedPlugins.put(propertyname, plugin);
        return plugin;
    }
