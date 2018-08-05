    public Sut getNewSutInstance() throws Exception {
        String defaultSutClassName = null;
        defaultSutClassName = JSystemProperties.getInstance().getPreference(FrameworkOptions.SUT_CLASS_NAME);
        if (defaultSutClassName == null) {
            defaultSutClassName = "jsystem.framework.sut.SutImpl";
            JSystemProperties.getInstance().setPreference(FrameworkOptions.SUT_CLASS_NAME, defaultSutClassName);
        }
        Class<?> c = Class.forName(defaultSutClassName);
        return (Sut) c.getConstructor(new Class[0]).newInstance(new Object[0]);
    }
