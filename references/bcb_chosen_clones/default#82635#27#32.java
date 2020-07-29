    static void jar(String... args) throws Exception {
        Class c = Class.forName("sun.tools.jar.Main", true, cl);
        Object instance = c.getConstructor(new Class[] { PrintStream.class, PrintStream.class, String.class }).newInstance(System.out, System.err, "jar");
        boolean result = (Boolean) c.getMethod("run", new Class[] { String[].class }).invoke(instance, new Object[] { args });
        if (!result) throw new Exception("jar failed");
    }
