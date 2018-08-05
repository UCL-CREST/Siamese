    private Collection<Class<? extends Plugin>> loadFromResource(ClassLoader classLoader, String resource) throws IOException {
        Collection<Class<? extends Plugin>> pluginClasses = new HashSet<Class<? extends Plugin>>();
        Enumeration providerFiles = classLoader.getResources(resource);
        if (!providerFiles.hasMoreElements()) {
            logger.warning("Can't find the resource: " + resource);
            return pluginClasses;
        }
        do {
            URL url = (URL) providerFiles.nextElement();
            InputStream stream = url.openStream();
            BufferedReader reader;
            try {
                reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            } catch (IOException e) {
                continue;
            }
            String line;
            while ((line = reader.readLine()) != null) {
                int index = line.indexOf('#');
                if (index != -1) {
                    line = line.substring(0, index);
                }
                line = line.trim();
                if (line.length() > 0) {
                    Class pluginClass;
                    try {
                        pluginClass = classLoader.loadClass(line);
                    } catch (ClassNotFoundException e) {
                        logger.log(Level.WARNING, "Can't use the Pluginclass with the name " + line + ".", e);
                        continue;
                    }
                    if (Plugin.class.isAssignableFrom(pluginClass)) {
                        pluginClasses.add((Class<? extends Plugin>) pluginClass);
                    } else {
                        logger.warning("The Pluginclass with the name " + line + " isn't a subclass of Plugin.");
                    }
                }
            }
            reader.close();
            stream.close();
        } while (providerFiles.hasMoreElements());
        return pluginClasses;
    }
