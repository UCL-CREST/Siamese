    @Before
    public void before() throws Throwable {
        Constructor<DynamicBean> constructor = dynamicBeanClass.getConstructor(ModelElement.class);
        dynamicBean = constructor.newInstance((ModelElement) null);
        myPropertyChangeListener = new MyPropertyChangeListener(dynamicBean);
    }
