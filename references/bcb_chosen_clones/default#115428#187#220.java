    private static void displayProviders(String strProviderTypeName) {
        Class providerClass = null;
        Method providersMethod = null;
        for (int i = 0; i < sm_configurationSources.length; i++) {
            String strServiceClassName = ((String[]) sm_configurationSources[i])[0];
            String strProvidersMethodName = ((String[]) sm_configurationSources[i])[1];
            try {
                Class serviceClass = Class.forName(strServiceClassName);
                providersMethod = serviceClass.getMethod(strProvidersMethodName, new Class[] { Class.class });
                break;
            } catch (Exception e) {
                if (DEBUG) {
                    out(e);
                }
            }
        }
        try {
            providerClass = Class.forName(strProviderTypeName);
        } catch (ClassNotFoundException e) {
            out(e);
        }
        Iterator services = null;
        try {
            services = (Iterator) providersMethod.invoke(null, new Object[] { providerClass });
        } catch (Throwable e) {
            out(e);
        }
        if (services != null) {
            while (services.hasNext()) {
                Object provider = services.next();
                out(3, provider.getClass().getName());
            }
        }
    }
