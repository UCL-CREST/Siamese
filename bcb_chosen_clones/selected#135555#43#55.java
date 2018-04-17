    @SuppressWarnings("unchecked")
    public <T extends Configurable> T getConfigurableComponent(WebDuffConfiguration componentConf) {
        T component = null;
        try {
            Class<?> theClass = WebDuffConfiguration.class.getClassLoader().loadClass(componentConf.getConf().getString("class"));
            Constructor<?> aConstructor = theClass.getConstructor();
            component = (T) aConstructor.newInstance();
            component.init(componentConf);
        } catch (Exception e) {
            throw new RuntimeException("Some problem making component", e);
        }
        return component;
    }
