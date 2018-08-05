    protected void parsePluginInfo() {
        Vector plugins = getPluginsFromDir(pluginDir);
        for (int i = 0; i < plugins.size(); ++i) {
            try {
                Class contClass = (Class) plugins.elementAt(i);
                Constructor[] constructors = contClass.getConstructors();
                Object cont = new Object();
                for (int j = 0; j < constructors.length; ++j) {
                    if (constructors[j].getParameterTypes().length == 1) {
                        cont = constructors[j].newInstance(new Object[] { null });
                        break;
                    }
                }
                String name = cont.getClass().getName();
                logger.finer("Checking plugin container class " + name);
                if (cont instanceof IChainable) {
                    String[] taskNames = ((IChainable) cont).getSupportedTasks();
                    logger.fine("Container class " + contClass.getName() + " with IChainable interface found!");
                    registry.add(contClass, taskNames);
                }
                if (name.equals("com.pallas.unicore.client.plugins.script.ScriptContainer")) {
                    registry.add(contClass, new String[] { OMGTask.SCRIPT_TASK });
                }
            } catch (java.lang.NoClassDefFoundError t) {
                logger.severe("Error occured while retrieving information about supported tasks!");
                logger.severe(t.getMessage());
            } catch (Exception ex) {
                logger.severe("Error occured while retrieving information about supported tasks!");
                logger.severe(ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
