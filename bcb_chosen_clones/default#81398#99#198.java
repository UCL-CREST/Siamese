    public static void main(String[] args) {
        if (args.length > 2) {
            OUTPUTFILE = args[0];
            MANIFESTFILE = args[1];
            TESTSUITECLASS = args[2];
            if (args.length > 3) {
                ANDROID_MAKE_FILE = args[3];
            }
        } else {
            System.out.println("usage: \n" + "\t... CollectAllTests <output-file> <manifest-file> <testsuite-class-name> <makefile-file>");
            System.exit(1);
        }
        if (ANDROID_MAKE_FILE.length() > 0) {
            testType = getTestType(ANDROID_MAKE_FILE);
        }
        Document manifest = null;
        try {
            manifest = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new FileInputStream(MANIFESTFILE));
        } catch (Exception e) {
            System.err.println("cannot open manifest");
            e.printStackTrace();
            System.exit(1);
            ;
        }
        Element documentElement = manifest.getDocumentElement();
        documentElement.getAttribute("package");
        xmlName = new File(OUTPUTFILE).getName();
        runner = getElementAttribute(documentElement, "instrumentation", "android:name");
        packageName = documentElement.getAttribute("package");
        target = getElementAttribute(documentElement, "instrumentation", "android:targetPackage");
        Class<?> testClass = null;
        try {
            testClass = Class.forName(TESTSUITECLASS);
        } catch (ClassNotFoundException e) {
            System.err.println("test class not found");
            e.printStackTrace();
            System.exit(1);
            ;
        }
        Method method = null;
        try {
            method = testClass.getMethod("suite", new Class<?>[0]);
        } catch (SecurityException e) {
            System.err.println("failed to get suite method");
            e.printStackTrace();
            System.exit(1);
            ;
        } catch (NoSuchMethodException e) {
            System.err.println("failed to get suite method");
            e.printStackTrace();
            System.exit(1);
            ;
        }
        try {
            TESTSUITE = (Test) method.invoke(null, (Object[]) null);
        } catch (IllegalArgumentException e) {
            System.err.println("failed to get suite method");
            e.printStackTrace();
            System.exit(1);
            ;
        } catch (IllegalAccessException e) {
            System.err.println("failed to get suite method");
            e.printStackTrace();
            System.exit(1);
            ;
        } catch (InvocationTargetException e) {
            System.err.println("failed to get suite method");
            e.printStackTrace();
            System.exit(1);
            ;
        }
        try {
            xmlGenerator = new MyXMLGenerator(OUTPUTFILE + ".xml");
        } catch (ParserConfigurationException e) {
            System.err.println("Can't initialize XML Generator");
            System.exit(1);
        }
        testCases = new LinkedHashMap<String, TestClass>();
        CollectAllTests cat = new CollectAllTests();
        cat.compose();
        if (!failed.isEmpty()) {
            System.err.println("The following classes have no default constructor");
            for (Iterator<String> iterator = failed.iterator(); iterator.hasNext(); ) {
                String type = iterator.next();
                System.err.println(type);
            }
            System.exit(1);
        }
        for (Iterator<TestClass> iterator = testCases.values().iterator(); iterator.hasNext(); ) {
            TestClass type = iterator.next();
            xmlGenerator.addTestClass(type);
        }
        try {
            xmlGenerator.dump();
        } catch (Exception e) {
            System.err.println("cannot dump xml");
            e.printStackTrace();
            System.exit(1);
        }
    }
