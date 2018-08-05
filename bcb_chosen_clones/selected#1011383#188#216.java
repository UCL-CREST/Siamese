    private void establishPostProcessingServices(User user) throws Exception {
        if (includePlugins(user) == false) {
            return;
        }
        PluginsModel pluginsModel = browserConfigurationModel.getPluginsModel();
        ArrayList pluginDescriptions = pluginsModel.getPluginDescriptions();
        int numberOfPluginDescriptions = pluginDescriptions.size();
        ArrayList plugins = new ArrayList();
        for (int i = 0; i < numberOfPluginDescriptions; i++) {
            PluginDescription pluginDescription = (PluginDescription) pluginDescriptions.get(i);
            String pluginClassName = pluginDescription.getPluginDescriptionClassName();
            Class postProcessingServiceClass = null;
            if (testMode.booleanValue() == false) {
                postProcessingServiceClass = Class.forName(pluginClassName);
            } else {
                ClassLoader classLoader = getClass().getClassLoader();
                ClassLoaderUtility classLoaderUtility = new ClassLoaderUtility(classLoader);
                classLoaderUtility.addURLs(pluginDescription.getDependencies());
                postProcessingServiceClass = classLoaderUtility.findClass(pluginClassName);
            }
            Constructor constructor = postProcessingServiceClass.getConstructor(new Class[0]);
            PostProcessingService service = (PostProcessingService) constructor.newInstance(new Object[0]);
            String serviceName = service.getName();
            if (securityService.canExecute(serviceName, user) == true) {
                plugins.add(service);
            }
        }
        postProcessingServices = (PostProcessingService[]) plugins.toArray(new PostProcessingService[0]);
    }
