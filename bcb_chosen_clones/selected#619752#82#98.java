    protected SystemConfiguration createAgentSystemConfiguration(ExtendedProperties<String, String> systemExtProps) throws AgSysLibException {
        SystemConfiguration systemConfiguration;
        String configurationClassName = extProps.getString("system.configuration.class", null);
        if (configurationClassName == null) {
            systemConfiguration = createSystemConfiguration(systemExtProps);
        } else {
            try {
                Class<?> configurationClass = Class.forName(configurationClassName);
                Constructor<?> initializerCtor = configurationClass.getConstructor(AbstractInitializer.class, ExtendedProperties.class);
                systemConfiguration = (SystemConfiguration) initializerCtor.newInstance(this, systemExtProps);
            } catch (Exception e) {
                throw new AgSysLibException("Cannot create tspSystemConfiguration.", e);
            }
        }
        systemConfiguration.initialize();
        return systemConfiguration;
    }
