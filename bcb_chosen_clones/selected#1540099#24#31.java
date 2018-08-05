    @SuppressWarnings("unchecked")
    public TestInterpreter getTestInterpreter(XMLConfiguration config, String section, int i) throws SecurityException, ClassNotFoundException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
        String testResultsInterpreterClass = config.getString(section + ".testInterpreters.testInterpreter(" + i + ").class");
        Constructor[] a = Class.forName(testResultsInterpreterClass).getConstructors();
        TestInterpreter tri = (TestInterpreter) a[0].newInstance();
        tri.configTestInterpreter(config, section + ".testInterpreters.testInterpreter(" + i + ")");
        return tri;
    }
