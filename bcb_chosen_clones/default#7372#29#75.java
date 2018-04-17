    public static void main(String argv[]) {
        sun.misc.CVM.setURLConnectionDefaultUseCaches(false);
        final String s = System.getProperty("java.class.path");
        System.out.println("java.class.path: " + s);
        URL[] urls = new URL[1];
        URLStreamHandlerFactory factory = new Factory();
        URLStreamHandler fileHandler = factory.createURLStreamHandler("file");
        try {
            urls[0] = new URL("file", "", -1, s, fileHandler);
        } catch (MalformedURLException e) {
            throw new InternalError();
        }
        int i;
        for (i = 0; i < Integer.parseInt(argv[0]); i++) {
            try {
                URLClassLoader cl = new URLClassLoader(urls, null);
                Class clazz = cl.loadClass(argv[1]);
                Class[] argTypes = new Class[1];
                argTypes[0] = Class.forName("[Ljava.lang.String;");
                Method main = clazz.getMethod("main", argTypes);
                Object[] args = new Object[1];
                String[] arg0 = new String[argv.length - 2];
                args[0] = arg0;
                for (int j = 0; j < argv.length - 2; j++) {
                    arg0[j] = argv[j + 2];
                }
                main.invoke(null, args);
                InputStream stream = cl.getResourceAsStream("Test.class");
                System.out.println("InputStream: " + stream);
                cl = null;
                clazz = null;
                main = null;
                stream = null;
                System.out.println("Running GC #1");
                java.lang.Runtime.getRuntime().gc();
                System.out.println("Running Finalizers #1");
                java.lang.Runtime.getRuntime().runFinalization();
                System.out.println("Running GC #2");
                java.lang.Runtime.getRuntime().gc();
                System.out.println("Running Finalizers #2");
                java.lang.Runtime.getRuntime().runFinalization();
                System.out.println("Done");
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
