    public void test_proxiedSetAndGet() throws Exception {
        Class<? extends StorableTestBasic> wrapperClass = StorableGenerator.getWrappedClass(StorableTestBasic.class);
        TestStorables.InvocationTracker props = new TestStorables.InvocationTracker("props");
        TestStorables.InvocationTracker handler = new TestStorables.InvocationTracker("handler");
        StorableTestBasic wrapper = wrapperClass.getConstructor(WrappedSupport.class, Storable.class).newInstance(handler, props);
        setPrimaryKeyProperties(wrapper);
        setBasicProperties(wrapper);
        wrapper.getStringProp();
        wrapper.getIntProp();
        wrapper.getLongProp();
        wrapper.getDoubleProp();
        wrapper.getId();
        for (Method method : Storable.class.getMethods()) {
            if (method.getParameterTypes().length > 0) {
                if (method.getParameterTypes()[0] != String.class) {
                    method.invoke(wrapper, wrapper);
                }
            } else {
                method.invoke(wrapper, (Object[]) null);
            }
        }
        props.assertTrack(TestStorables.ALL_GET_METHODS | TestStorables.ALL_SET_METHODS | TestStorables.sCopy | TestStorables.sToStringKeyOnly | TestStorables.sHasDirtyProperties | TestStorables.sEqualKeys | TestStorables.sEqualProperties | TestStorables.sMarkPropertiesClean | TestStorables.sMarkAllPropertiesClean | TestStorables.sMarkPropertiesDirty | TestStorables.sMarkAllPropertiesDirty | TestStorables.ALL_COPY_PROP_METHODS);
        handler.assertTrack(TestStorables.sTryLoad | TestStorables.sLoad | TestStorables.sInsert | TestStorables.sTryInsert | TestStorables.sUpdate | TestStorables.sTryUpdate | TestStorables.sDelete | TestStorables.sTryDelete);
    }
