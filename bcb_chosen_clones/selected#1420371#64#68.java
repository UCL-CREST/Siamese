    private void setInvocationHandler(Object testInstance) throws Exception {
        Class<?> powerMockTestNGMethodHandlerClass = Class.forName(PowerMockTestNGMethodHandler.class.getName(), false, mockLoader);
        Object powerMockTestNGMethodHandlerInstance = powerMockTestNGMethodHandlerClass.getConstructor(Class.class).newInstance(testInstance.getClass());
        Whitebox.invokeMethod(testInstance, "setHandler", powerMockTestNGMethodHandlerInstance);
    }
