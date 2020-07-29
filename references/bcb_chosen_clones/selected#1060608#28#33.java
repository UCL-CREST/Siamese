    @Override
    protected Object getDefaultVirtualDataObject(WidgetInfo widget) throws Exception {
        ClassLoader classLoader = JavaInfoUtils.getClassLoader(this);
        Class<?> dataClass = classLoader.loadClass("com.gwtext.client.widgets.layout.AnchorLayoutData");
        return ReflectionUtils.getConstructor(dataClass, String.class).newInstance("100% 0");
    }
