    private void listAndInstantiateServiceProviders() {
        final Enumeration<URL> resources = ClassLoaderHelper.getResources(SERVICES_FILE, ServiceManager.class);
        String name;
        try {
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                InputStream stream = url.openStream();
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(stream), 100);
                    name = reader.readLine();
                    while (name != null) {
                        name = name.trim();
                        if (!name.startsWith("#")) {
                            final ServiceProvider<?> serviceProvider = ClassLoaderHelper.instanceFromName(ServiceProvider.class, name, ServiceManager.class, "service provider");
                            @SuppressWarnings("unchecked") final Class<ServiceProvider<?>> serviceProviderClass = (Class<ServiceProvider<?>>) serviceProvider.getClass();
                            managedProviders.put(serviceProviderClass, new ServiceProviderWrapper(serviceProvider));
                        }
                        name = reader.readLine();
                    }
                } finally {
                    stream.close();
                }
            }
        } catch (IOException e) {
            throw new SearchException("Unable to read " + SERVICES_FILE, e);
        }
    }
