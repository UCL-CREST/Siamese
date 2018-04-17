    public static Test suite() {
        TestSuite suite = new TestSuite();
        for (int x = 0; x < PACKAGES.length; x++) {
            String name = PACKAGES[x] + "._";
            Method suiteMethod;
            try {
                Class clazz = Class.forName(name);
                suiteMethod = clazz.getMethod("suite", new Class[] {});
                TestSuite suiteToAdd = (TestSuite) suiteMethod.invoke(null, new Object[] {});
                Enumeration tests = suiteToAdd.tests();
                while (tests.hasMoreElements()) {
                    Test t = (Test) tests.nextElement();
                    suite.addTest(t);
                }
            } catch (Exception e) {
                System.err.println("package test " + name + " not found");
            }
        }
        return suite;
    }
