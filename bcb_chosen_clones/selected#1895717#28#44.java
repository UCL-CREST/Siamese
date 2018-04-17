    public PolicyResolverRegistry(ConfigElement config) {
        logger = LogFactory.getLogger();
        if (config != null) {
            ConfigElement[] items = (ConfigElement[]) config.getXMLElementsByType(ELEMTYPE_POLICYRESOLVER);
            for (ConfigElement each : items) {
                String className = (String) each.getAttributeValueByName(ATTR_POLICYRESOLVER_CLASSNAME);
                try {
                    Class<?> clazz = Class.forName(className);
                    Constructor<?> cons = clazz.getConstructor(each.getClass());
                    PolicyResolver policyResolver = (PolicyResolver) cons.newInstance(each);
                    register(policyResolver);
                } catch (Exception ex) {
                    logger.error("Error occurs while loading policy resolver : " + className + ", will continue to load next.", ex);
                }
            }
        }
    }
