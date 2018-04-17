    public static void main(String[] args) {
        JRIClassLoader mcl = JRIClassLoader.getMainLoader();
        String nl = findNativeLibrary("boot", false);
        if (nl == null) {
            System.err.println("ERROR: Unable to locate native bootstrap library.");
            System.exit(1);
        }
        mcl.registerLibrary("boot", new File(nl));
        String cp = System.getProperty("java.class.path");
        StringTokenizer st = new StringTokenizer(cp, File.pathSeparator);
        while (st.hasMoreTokens()) {
            String p = st.nextToken();
            mcl.addClassPath(p);
            if (bootFile == null && (new File(p)).isFile()) bootFile = p;
        }
        try {
            Class stage2class = mcl.findAndLinkClass("JRIBootstrap");
            Method m = stage2class.getMethod("bootstrap", new Class[] { String[].class });
            m.invoke(null, new Object[] { args });
        } catch (Exception rtx) {
            System.err.println("ERROR: Unable to invoke bootstrap method in JRIBootstrap! (" + rtx + ")");
            rtx.printStackTrace();
            System.exit(2);
        }
    }
