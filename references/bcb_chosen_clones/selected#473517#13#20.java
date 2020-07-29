    public static TestSuite suite() throws Exception {
        Class testClass = ClassLoader.getSystemClassLoader().loadClass("org.w3c.domts.level1.core.alltests");
        Constructor testConstructor = testClass.getConstructor(new Class[] { DOMTestDocumentBuilderFactory.class });
        DocumentBuilderFactory gnujaxpFactory = (DocumentBuilderFactory) ClassLoader.getSystemClassLoader().loadClass("gnu.xml.dom.JAXPFactory").newInstance();
        DOMTestDocumentBuilderFactory factory = new JAXPDOMTestDocumentBuilderFactory(gnujaxpFactory, JAXPDOMTestDocumentBuilderFactory.getConfiguration1());
        Object test = testConstructor.newInstance(new Object[] { factory });
        return new JUnitTestSuiteAdapter((DOMTestSuite) test);
    }
