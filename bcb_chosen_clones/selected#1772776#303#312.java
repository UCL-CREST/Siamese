    public boolean constructorTest() throws Exception {
        GrandTestAuto gta = Helpers.setupForZip(Grandtestauto.test4_zip);
        Class<?> ut = Class.forName("a4.test.UnitTester");
        Field flag = ut.getDeclaredField("flag");
        assert !flag.getBoolean(null) : "Flag not initially false";
        Constructor constructor = ut.getConstructor(GrandTestAuto.class);
        CoverageUnitTester cut = (CoverageUnitTester) constructor.newInstance(gta);
        assert !cut.runTests();
        return true;
    }
