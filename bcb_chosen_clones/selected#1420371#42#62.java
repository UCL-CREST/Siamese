    public Object newInstance(@SuppressWarnings("rawtypes") Constructor constructor, Object... params) {
        MockRepository.clear();
        Class<?> testClass = constructor.getDeclaringClass();
        mockLoader.addIgnorePackage(ignorePackagesExtractor.getPackagesToIgnore(testClass));
        mockLoader.addClassesToModify(testClassesExtractor.getTestClasses(testClass));
        try {
            registerProxyframework(mockLoader);
            new MockPolicyInitializerImpl(testClass).initialize(mockLoader);
            final Class<?> testClassLoadedByMockedClassLoader = createTestClass(testClass);
            Constructor<?> con = testClassLoadedByMockedClassLoader.getConstructor(constructor.getParameterTypes());
            final Object testInstance = con.newInstance(params);
            if (!extendsPowerMockTestCase(testClass)) {
                setInvocationHandler(testInstance);
            }
            return testInstance;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
