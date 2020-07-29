    public static boolean runTest(String test) {
        trace("The benchmark is: " + test);
        try {
            Class test_class = Class.forName(test);
            Method main = test_class.getMethod("main", new Class[] { String[].class });
            long itime = System.currentTimeMillis();
            main.invoke(null, new Object[] { new String[] {} });
            System.out.println("The test run: " + (System.currentTimeMillis() - itime) + " ms");
        } catch (Throwable e) {
            System.out.println(test + " failed, " + e);
            return false;
        }
        return true;
    }
