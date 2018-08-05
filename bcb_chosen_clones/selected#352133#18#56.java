    public ArrayList loadPluginClasses(URL libraryDirectoryURL) throws PedroException {
        ArrayList plugins = new ArrayList();
        try {
            if (libraryDirectoryURL == null) {
                return plugins;
            }
            ModelClassLoader modelClassLoader = ModelClassLoader.getModelClassLoader();
            PluginFileFilter pluginFileFilter = new PluginFileFilter();
            File libraryDirectory = new File(libraryDirectoryURL.getFile());
            File[] files = libraryDirectory.listFiles(pluginFileFilter);
            for (int i = 0; i < files.length; i++) {
                JarFile currentJarFile = new JarFile(files[i]);
                Enumeration pluginFileList = currentJarFile.entries();
                while (pluginFileList.hasMoreElements() == true) {
                    ZipEntry currentEntry = (ZipEntry) pluginFileList.nextElement();
                    String fileName = currentEntry.getName();
                    if (fileName.endsWith(".class") == true) {
                        fileName = fileName.replace('/', '.');
                        int classIndex = fileName.indexOf(".class");
                        String className = fileName.substring(0, classIndex);
                        Class cls = modelClassLoader.loadClass(className);
                        if (cls != null) {
                            Class[] interfaces = cls.getInterfaces();
                            for (int j = 0; j < interfaces.length; j++) {
                                if (PedroPlugin.class.isAssignableFrom(interfaces[j]) == true) {
                                    Constructor constructor = cls.getConstructor(new Class[0]);
                                    PedroPlugin pedroPlugin = (PedroPlugin) constructor.newInstance(new Object[0]);
                                    plugins.add(pedroPlugin);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception err) {
            throw new PedroException(err.toString());
        }
        return plugins;
    }
