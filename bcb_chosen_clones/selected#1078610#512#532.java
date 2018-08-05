    private void rerunTest(Test test) {
        if (!(test instanceof TestCase)) {
            showInfo("Could not reload " + test.toString());
            return;
        }
        Test reloadedTest = null;
        try {
            Class reloadedTestClass = getLoader().reload(test.getClass());
            Class[] classArgs = { String.class };
            Object[] args = new Object[] { ((TestCase) test).getName() };
            Constructor constructor = reloadedTestClass.getConstructor(classArgs);
            reloadedTest = (Test) constructor.newInstance(args);
        } catch (Exception e) {
            showInfo("Could not reload " + test.toString());
            return;
        }
        TestResult result = new TestResult();
        reloadedTest.run(result);
        String message = reloadedTest.toString();
        if (result.wasSuccessful()) showInfo(message + " was successful"); else if (result.errorCount() == 1) showStatus(message + " had an error"); else showStatus(message + " had a failure");
    }
