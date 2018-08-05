    public IncrementalSAXSource_Xerces() throws NoSuchMethodException {
        try {
            Class xniConfigClass = ObjectFactory.findProviderClass("com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration", ObjectFactory.findClassLoader(), true);
            Class[] args1 = { xniConfigClass };
            Constructor ctor = SAXParser.class.getConstructor(args1);
            Class xniStdConfigClass = ObjectFactory.findProviderClass("com.sun.org.apache.xerces.internal.parsers.StandardParserConfiguration", ObjectFactory.findClassLoader(), true);
            fPullParserConfig = xniStdConfigClass.newInstance();
            Object[] args2 = { fPullParserConfig };
            fIncrementalParser = (SAXParser) ctor.newInstance(args2);
            Class fXniInputSourceClass = ObjectFactory.findProviderClass("com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource", ObjectFactory.findClassLoader(), true);
            Class[] args3 = { fXniInputSourceClass };
            fConfigSetInput = xniStdConfigClass.getMethod("setInputSource", args3);
            Class[] args4 = { String.class, String.class, String.class };
            fConfigInputSourceCtor = fXniInputSourceClass.getConstructor(args4);
            Class[] args5 = { java.io.InputStream.class };
            fConfigSetByteStream = fXniInputSourceClass.getMethod("setByteStream", args5);
            Class[] args6 = { java.io.Reader.class };
            fConfigSetCharStream = fXniInputSourceClass.getMethod("setCharacterStream", args6);
            Class[] args7 = { String.class };
            fConfigSetEncoding = fXniInputSourceClass.getMethod("setEncoding", args7);
            Class[] argsb = { Boolean.TYPE };
            fConfigParse = xniStdConfigClass.getMethod("parse", argsb);
            Class[] noargs = new Class[0];
            fReset = fIncrementalParser.getClass().getMethod("reset", noargs);
        } catch (Exception e) {
            IncrementalSAXSource_Xerces dummy = new IncrementalSAXSource_Xerces(new SAXParser());
            this.fParseSomeSetup = dummy.fParseSomeSetup;
            this.fParseSome = dummy.fParseSome;
            this.fIncrementalParser = dummy.fIncrementalParser;
        }
    }
