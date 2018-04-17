    public static void main(String[] args) {
        final String version = System.getProperty("java.version");
        if (version.startsWith("1.0") || version.startsWith("1.1") || version.startsWith("1.2") || version.startsWith("1.3")) {
            System.err.println("");
            System.err.print("J requires Java 1.4 or later.");
            System.err.println(" (Java 1.4.2 is recommended.)");
            System.err.println("");
            System.exit(1);
        }
        try {
            Class c = Class.forName("org.armedbear.j.Editor");
            Class[] parameterTypes = new Class[1];
            parameterTypes[0] = String[].class;
            Method method = c.getMethod("main", parameterTypes);
            Object[] parameters = new Object[1];
            parameters[0] = args;
            method.invoke(null, parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
