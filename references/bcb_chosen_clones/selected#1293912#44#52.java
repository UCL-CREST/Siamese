    @Override
    protected PowerMockJUnit3RunnerDelegate createDelegatorFromClassloader(MockClassLoader classLoader, Class<?> testClass, final List<Method> methodsToTest) throws Exception {
        final Class<?> testClassLoadedByMockedClassLoader = classLoader.loadClass(testClass.getName());
        Class<?> delegateClass = classLoader.loadClass(PowerMockJUnit3RunnerDelegateImpl.class.getName());
        Constructor<?> con = delegateClass.getConstructor(new Class[] { Class.class, Method[].class });
        final PowerMockJUnit3RunnerDelegate newDelegate = (PowerMockJUnit3RunnerDelegate) con.newInstance(new Object[] { testClassLoadedByMockedClassLoader, methodsToTest.toArray(new Method[0]) });
        newDelegate.setName(name);
        return newDelegate;
    }
