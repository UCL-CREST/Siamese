    public static void main(String[] args) throws Exception {
        Class testClass = AllTests.class;
        ArrayList<Method> setups = new ArrayList<Method>();
        ArrayList<Method> tearDowns = new ArrayList<Method>();
        for (Method method : testClass.getDeclaredMethods()) {
            int modifiers = method.getModifiers();
            if (Modifier.isPublic(modifiers) && !Modifier.isStatic(modifiers) && method.getAnnotation(Ignore.class) == null) {
                if (method.getAnnotation(Before.class) != null) {
                    setups.add(method);
                }
                if (method.getAnnotation(After.class) != null) {
                    setups.add(method);
                }
            }
        }
        System.out.println("Starting all tests.");
        Object instance = testClass.newInstance();
        for (Method method : testClass.getDeclaredMethods()) {
            int modifiers = method.getModifiers();
            if (Modifier.isPublic(modifiers) && !Modifier.isStatic(modifiers) && method.getAnnotation(Ignore.class) == null) {
                Test testAnnotation = method.getAnnotation(Test.class);
                if (testAnnotation != null) {
                    for (Method setup : setups) {
                        setup.invoke(instance, (Object[]) null);
                    }
                    Class expectedException = testAnnotation.expected();
                    if (expectedException.getName().equals("org.junit.Test$None")) {
                        expectedException = null;
                    }
                    try {
                        method.invoke(instance, (Object[]) null);
                    } catch (Exception e) {
                        if (expectedException == null) {
                            System.out.println(testClass.getName() + "." + method.getName() + ": " + e.getCause().getMessage());
                            new BufferedReader(new InputStreamReader(System.in)).readLine();
                        } else {
                            if (!e.getCause().getClass().equals(testAnnotation.expected())) {
                                System.out.println(testClass.getName() + "." + method.getName() + ": " + "Exception expected: " + testAnnotation.expected().getName() + ", Exception thrown: " + e.getCause().getMessage());
                                new BufferedReader(new InputStreamReader(System.in)).readLine();
                            }
                            expectedException = null;
                        }
                    }
                    if (expectedException != null) {
                        System.out.println(testClass.getName() + "." + method.getName() + ": " + "Expected exception not thrown: " + testAnnotation.expected().getName());
                        new BufferedReader(new InputStreamReader(System.in)).readLine();
                    }
                    for (Method tearDown : tearDowns) {
                        tearDown.invoke(instance, (Object[]) null);
                    }
                }
            }
        }
        System.out.println("Done with all tests.");
    }
