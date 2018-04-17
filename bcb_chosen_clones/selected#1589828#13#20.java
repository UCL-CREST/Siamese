    public static TestSuite suite() throws Exception {
        Class testClass = ClassLoader.getSystemClassLoader().loadClass("org.w3c.domts.level1.core.alltests");
        Constructor testConstructor = testClass.getConstructor(new Class[] { DOMTestDocumentBuilderFactory.class });
        DocumentBuilderFactory crimsonFactory = (DocumentBuilderFactory) ClassLoader.getSystemClassLoader().loadClass("org.apache.crimson.jaxp.DocumentBuilderFactoryImpl").newInstance();
        DOMTestDocumentBuilderFactory factory = new JAXPDOMTestDocumentBuilderFactory(crimsonFactory, JAXPDOMTestDocumentBuilderFactory.getConfiguration1());
        Object test = testConstructor.newInstance(new Object[] { factory });
        return new JUnitTestSuiteAdapter((DOMTestSuite) test);
    }
