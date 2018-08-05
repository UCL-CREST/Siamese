    public Set<Plugin<?>> loadPluginMetaData() throws PluginRegistryException {
        try {
            final Enumeration<URL> urls = JavaSystemHelper.getResources(pluginMetaInfPath);
            pluginsSet.clear();
            if (urls != null) {
                while (urls.hasMoreElements()) {
                    final URL url = urls.nextElement();
                    echoMessages.add(PluginMessageBundle.getMessage("plugin.info.visitor.resource.found", "interfaces", url.getPath()));
                    InputStream resourceInput = null;
                    Reader reader = null;
                    BufferedReader buffReader = null;
                    String line;
                    try {
                        resourceInput = url.openStream();
                        reader = new InputStreamReader(resourceInput);
                        buffReader = new BufferedReader(reader);
                        line = buffReader.readLine();
                        while (line != null) {
                            try {
                                if (!StringHelper.isEmpty(line)) {
                                    echoMessages.add(PluginMessageBundle.getMessage("plugin.info.visitor.resource.processing", "interface", line));
                                    pluginsSet.add(inspectPlugin(Class.forName(line.trim())));
                                }
                                line = buffReader.readLine();
                            } catch (final ClassNotFoundException cnfe) {
                                throw new PluginRegistryException("plugin.error.load.classnotfound", cnfe, pluginMetaInfPath, line);
                            }
                        }
                    } catch (final IOException ioe) {
                        throw new PluginRegistryException("plugin.error.load.ioe", ioe, url.getFile() + "\n" + url.toString(), ioe.getMessage());
                    } finally {
                        if (buffReader != null) {
                            buffReader.close();
                        }
                        if (reader != null) {
                            reader.close();
                        }
                        if (resourceInput != null) {
                            resourceInput.close();
                        }
                    }
                }
            }
            return Collections.unmodifiableSet(pluginsSet);
        } catch (final IOException ioe) {
            throw new PluginRegistryException("plugin.error.load.ioe", ioe, pluginMetaInfPath, ioe.getMessage());
        }
    }
