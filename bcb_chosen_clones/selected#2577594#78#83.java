    private TestCase createTest(Class<?> testClass, Method method) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Constructor<?> constructor = testClass.getConstructor();
        TestCase test = (TestCase) constructor.newInstance();
        test.setName(method.getName());
        return test;
    }
