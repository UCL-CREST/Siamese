    public static void main(String argv[]) {
        String inFileName = null;
        String xslFileName = null;
        String outFileName = null;
        java.util.Vector<String> params = new java.util.Vector<String>();
        for (int ii = 0; ii < argv.length; ii++) {
            if (argv[ii].equals("-IN")) {
                inFileName = argv[++ii];
            } else if (argv[ii].equals("-XSL")) {
                xslFileName = argv[++ii];
            } else if (argv[ii].equals("-OUT")) {
                outFileName = argv[++ii];
            } else if (argv[ii].equals("-PARAM")) {
                if (ii + 2 < argv.length) {
                    String name = argv[++ii];
                    params.addElement(name);
                    String expression = argv[++ii];
                    params.addElement(expression);
                } else {
                    showUsage();
                }
            } else {
                showUsage();
            }
        }
        if (inFileName == null || xslFileName == null || outFileName == null) {
            showUsage();
        }
        final String parserProperty = "javax.xml.transform.TransformerFactory";
        final String workaroundParser = "org.apache.xalan.processor.TransformerFactoryImpl";
        try {
            java.lang.Class cls = java.lang.Class.forName(workaroundParser);
            System.setProperty(parserProperty, workaroundParser);
            System.out.println("Info: jvmtiGen using " + parserProperty + " = " + workaroundParser);
        } catch (ClassNotFoundException cnfex) {
        }
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(true);
        try {
            File datafile = new File(inFileName);
            File stylesheet = new File(xslFileName);
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(datafile);
            TransformerFactory tFactory = TransformerFactory.newInstance();
            StreamSource stylesource = new StreamSource(stylesheet);
            Transformer transformer = tFactory.newTransformer(stylesource);
            for (int ii = 0; ii < params.size(); ii += 2) {
                transformer.setParameter((String) params.elementAt(ii), (String) params.elementAt(ii + 1));
            }
            DOMSource source = new DOMSource(document);
            PrintStream ps = new PrintStream(new FileOutputStream(outFileName));
            StreamResult result = new StreamResult(ps);
            transformer.transform(source, result);
        } catch (TransformerConfigurationException tce) {
            System.out.println("\n** Transformer Factory error");
            System.out.println("   " + tce.getMessage());
            Throwable x = tce;
            if (tce.getException() != null) x = tce.getException();
            x.printStackTrace();
        } catch (TransformerException te) {
            System.out.println("\n** Transformation error");
            System.out.println("   " + te.getMessage());
            Throwable x = te;
            if (te.getException() != null) x = te.getException();
            x.printStackTrace();
        } catch (SAXException sxe) {
            Exception x = sxe;
            if (sxe.getException() != null) x = sxe.getException();
            x.printStackTrace();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
