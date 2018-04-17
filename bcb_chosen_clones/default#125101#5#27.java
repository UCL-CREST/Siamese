    public static boolean executeTests(Class c) {
        PPL_Test.initialize();
        Boolean single_test_result_ok;
        Boolean global_test_result_ok = new Boolean(true);
        System.out.println("Checking " + c.getName());
        Method methods[] = c.getDeclaredMethods();
        for (Method currentMethod : methods) {
            try {
                if (currentMethod.getName().startsWith("test")) {
                    System.out.println("Executing " + currentMethod.getName());
                    single_test_result_ok = (Boolean) currentMethod.invoke(new Object(), new Object[0]);
                    if (!single_test_result_ok) {
                        global_test_result_ok = new Boolean(false);
                        System.out.println(currentMethod.getName() + " failed");
                    }
                }
            } catch (Exception e) {
                System.out.println("An unexpected exception has occured");
                return new Boolean(false);
            }
        }
        return global_test_result_ok;
    }
