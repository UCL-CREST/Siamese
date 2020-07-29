    private static ClearsilverFactory newDefaultClearsilverFactory() {
        String factoryClassName = System.getProperty(DEFAULT_CS_FACTORY_CLASS_PROPERTY_NAME, DEFAULT_CS_FACTORY_CLASS_NAME);
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Class<ClearsilverFactory> clazz = loadClass(factoryClassName, classLoader);
            Constructor<ClearsilverFactory> constructor = clazz.getConstructor();
            return constructor.newInstance();
        } catch (Exception e) {
            String errMsg = "Unable to load default ClearsilverFactory class: \"" + factoryClassName + "\"";
            logger.log(Level.SEVERE, errMsg, e);
            throw new RuntimeException(errMsg, e);
        }
    }
