    public void process() {
        System.out.printf("\n> Processing %s\n", tester.getClass());
        boolean failed = false;
        for (Method m : tester.getClass().getDeclaredMethods()) {
            Test anno = m.getAnnotation(Test.class);
            Ignore ignore = m.getAnnotation(Ignore.class);
            if (anno != null) {
                if (ignore == null) {
                    System.out.println(">> Invoking test " + m.getName());
                    Object result;
                    try {
                        result = m.invoke(tester);
                    } catch (Exception e) {
                        throw new Error("Test invocation failed", e);
                    }
                    boolean ok = true;
                    if (Collection.class.isAssignableFrom(m.getReturnType())) {
                        ok = verifyResults((Collection) result, anno.result(), anno.ordered());
                    } else if (m.getReturnType() != void.class) {
                        ok = verifyResult(result, anno.result());
                    }
                    if (!ok) {
                        System.out.println(">>> Expected: " + anno);
                        System.out.println(">>> Got: " + result);
                        failed = true;
                    }
                } else {
                    System.out.println(">> Ignoring test " + m.getName());
                    if (ignore.value().length() > 0) {
                        System.out.println(">>> Reason: " + ignore.value());
                    }
                }
            }
        }
        if (failed) {
            throw new Error("Test(s) returned unexpected result");
        }
    }
