    private static List<TestSuite> createTestSuites(List<Class<? extends TestSuite>> testSuiteClassesFilter) throws ClassNotFoundException {
        logger.debug("Scanning classpath for TestSuites and TestCases");
        Collection<Class<?>> classes = ReflectUtil.listClassesInPackage("org.nightlabs.jfire.testsuite", true);
        logger.debug("Found " + classes.size() + " classes");
        List<Class<? extends TestSuite>> testSuiteClasses = new LinkedList<Class<? extends TestSuite>>();
        Map<Class<? extends TestSuite>, List<Class<? extends TestCase>>> suites2TestCases = new HashMap<Class<? extends TestSuite>, List<Class<? extends TestCase>>>();
        for (Class<?> clazz : classes) {
            if (TestSuite.class.isAssignableFrom(clazz)) {
                Class<? extends TestSuite> suiteClass = (Class<? extends TestSuite>) clazz;
                testSuiteClasses.add(suiteClass);
            } else if (TestCase.class.isAssignableFrom(clazz)) {
                if ((clazz.getModifiers() & Modifier.ABSTRACT) != 0) continue;
                Class<? extends TestCase> testCaseClass = (Class<? extends TestCase>) clazz;
                JFireTestSuite testSuiteAnnotation = clazz.getAnnotation(JFireTestSuite.class);
                Class<? extends TestSuite> suiteClass = DefaultTestSuite.class;
                if (testSuiteAnnotation != null) {
                    suiteClass = testSuiteAnnotation.value();
                }
                List<Class<? extends TestCase>> testCaseClasses = suites2TestCases.get(suiteClass);
                if (testCaseClasses == null) {
                    testCaseClasses = new LinkedList<Class<? extends TestCase>>();
                    suites2TestCases.put(suiteClass, testCaseClasses);
                }
                testCaseClasses.add(testCaseClass);
            }
        }
        for (Class<? extends TestSuite> suiteClass : suites2TestCases.keySet()) {
            if (!testSuiteClasses.contains(suiteClass)) {
                testSuiteClasses.add(suiteClass);
            }
        }
        if (testSuiteClassesFilter != null) {
            testSuiteClasses.retainAll(testSuiteClassesFilter);
        }
        List<TestSuite> runSuites = new LinkedList<TestSuite>();
        for (Class<? extends TestSuite> clazz : testSuiteClasses) {
            if (suites2TestCases.containsKey(clazz)) {
                List<Class<? extends TestCase>> testCaseClasses = suites2TestCases.get(clazz);
                Constructor<?> c = null;
                try {
                    c = clazz.getConstructor(new Class[] { Class[].class });
                } catch (Exception e) {
                    logger.error("Could not find (Class<? extends TestCase> ... classes) constructor for TestSuite " + clazz.getName(), e);
                    continue;
                }
                TestSuite testSuite = null;
                try {
                    testSuite = (TestSuite) c.newInstance(new Object[] { testCaseClasses.toArray(new Class[testCaseClasses.size()]) });
                } catch (Exception e) {
                    logger.error("Could not instantiate TestSuite " + clazz.getName(), e);
                    continue;
                }
                runSuites.add(testSuite);
            }
        }
        return runSuites;
    }
