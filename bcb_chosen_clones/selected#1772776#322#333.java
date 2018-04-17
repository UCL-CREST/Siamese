    private CoverageUnitTester cutForConfiguredPackage(String zipName, String packageName, String singlePackage, String singleTest, String initialTestMethod, String finalTestMethod, String singleTestMethod) {
        GrandTestAuto gta = Helpers.setupForZip(new File(zipName), true, true, true, null, null, singlePackage, false, true, Helpers.defaultLogFile().getPath(), null, null, null, singleTest, initialTestMethod, finalTestMethod, singleTestMethod);
        try {
            Class<?> ut = Class.forName(packageName + ".UnitTester");
            Constructor ctr = ut.getConstructor(GrandTestAuto.class);
            return (CoverageUnitTester) ctr.newInstance(gta);
        } catch (Exception e) {
            e.printStackTrace();
            assert false : "See above stack trace";
        }
        return null;
    }
