    public Set<Plugin<?>> loadPluginImplementationMetaData() throws PluginRegistryException {
        try {
            final Enumeration<URL> urls = JavaSystemHelper.getResources(pluginImplementationMetaInfPath);
            pluginImplsSet.clear();
            if (urls != null) {
                while (urls.hasMoreElements()) {
                    final URL url = urls.nextElement();
                    echoMessages.add(PluginMessageBundle.getMessage("plugin.info.visitor.resource.found", "classes", url.getPath()));
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
                                pluginImplsSet.add(inspectPluginImpl(Class.forName(line.trim())));
                                echoMessages.add(PluginMessageBundle.getMessage("plugin.info.visitor.resource.processing", "class", line));
                                line = buffReader.readLine();
                            } catch (final ClassNotFoundException cnfe) {
                                throw new PluginRegistryException("plugin.error.load.classnotfound", cnfe, pluginImplementationMetaInfPath, line);
                            } catch (final LinkageError ncfe) {
                                if (LOGGER.isDebugEnabled()) {
                                    echoMessages.add(PluginMessageBundle.getMessage("plugin.info.visitor.resource.linkageError", "class", line, ncfe.getMessage()));
                                }
                                line = buffReader.readLine();
                            }
                        }
                    } catch (final IOException ioe) {
                        throw new PluginRegistryException("plugin.error.load.ioe", ioe, url.getFile(), ioe.getMessage());
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
            return Collections.unmodifiableSet(pluginImplsSet);
        } catch (final IOException ioe) {
            throw new PluginRegistryException("plugin.error.load.ioe", ioe, pluginImplementationMetaInfPath, ioe.getMessage());
        }
    }
