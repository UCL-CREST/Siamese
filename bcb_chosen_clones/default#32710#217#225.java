    static void addClassPath(String s) {
        if (bootRJavaLoader == null) return;
        try {
            Method m = bootRJavaLoader.getClass().getMethod("addClassPath", new Class[] { String.class });
            m.invoke(bootRJavaLoader, new Object[] { s });
        } catch (Exception e) {
            System.err.println("FAILED: JRIBootstrap.addClassPath");
        }
    }
