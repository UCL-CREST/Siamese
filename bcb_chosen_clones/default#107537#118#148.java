    static int testNullBehavior() {
        int failures = 0;
        int count = 0;
        Method[] methods = OneCaseSwitches.class.getDeclaredMethods();
        try {
            for (Method method : methods) {
                count++;
                try {
                    if (method.isAnnotationPresent(TestMeForNull.class)) {
                        System.out.println("Testing method " + method);
                        method.invoke(null, (String) null, emptyStringSet, false);
                        failures++;
                        System.err.println("Didn't get NPE as expected from " + method);
                    }
                } catch (InvocationTargetException ite) {
                    Throwable targetException = ite.getTargetException();
                    if (!(targetException instanceof NullPointerException)) {
                        failures++;
                        System.err.println("Didn't get expected target exception NPE, got " + ite.getClass().getName());
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (count == 0) {
            failures++;
            System.err.println("Did not find any annotated methods.");
        }
        return failures;
    }
