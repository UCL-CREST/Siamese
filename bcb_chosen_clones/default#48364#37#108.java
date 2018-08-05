    public static void main(String[] args) throws Exception {
        debug = Boolean.getBoolean("SageScriptingFramework.debug");
        String mainClass = System.getProperty("jdna.MainClass", DEFAULT_MAIN_CLASS);
        if (debug) System.out.println("Using main class: " + mainClass);
        String home = System.getenv(HOME_DIR_VAR);
        if (home == null) {
            home = ".";
        }
        File homeDir = null;
        try {
            homeDir = new File(home).getCanonicalFile();
        } catch (IOException e2) {
            System.out.println("Failed to set home dir: " + home);
            System.exit(1);
        }
        if (debug) System.out.println("Assuming Home Dir: " + homeDir.getAbsolutePath());
        if (!homeDir.exists() && !homeDir.isDirectory()) {
            System.out.printf("Home Dir: %s does not exist, or is not a valid directory.\n", home);
            System.exit(1);
        }
        java.util.List<URL> urls = new ArrayList<URL>();
        File defClassDir = new File(homeDir, "bin");
        if (defClassDir.exists()) {
            if (debug) System.out.println("Adding Bin dir: " + defClassDir.getAbsolutePath());
            try {
                urls.add(defClassDir.toURI().toURL());
            } catch (MalformedURLException e1) {
                if (debug) e1.printStackTrace();
            }
        }
        File libDir = new File(homeDir, "JARs");
        File libs[] = libDir.listFiles();
        for (File lib : libs) {
            if (lib.getName().endsWith(".jar")) {
                if (debug) System.out.println("Adding Lib: " + lib.getAbsolutePath());
                urls.add(lib.toURI().toURL());
            }
        }
        urls.add(new File("scripts").toURI().toURL());
        urls.add(new File("scriptsLib").toURI().toURL());
        URLClassLoader cl = new URLClassLoader(urls.toArray(new URL[urls.size()]), ScriptingFrameworkLauncher.class.getClassLoader());
        if (debug) {
            for (URL u : cl.getURLs()) {
                System.out.printf("ClassLoader Url: %s\n", u.toExternalForm());
            }
        }
        Thread.currentThread().setContextClassLoader(cl);
        Class run = null;
        try {
            run = cl.loadClass(mainClass);
        } catch (ClassNotFoundException e) {
            System.out.printf("Failed to Find Class: %s\nPlease set the %s environment variable.\n", mainClass, HOME_DIR_VAR);
            System.exit(1);
        }
        Method m = null;
        try {
            m = run.getMethod("main", new Class[] { String[].class });
        } catch (Exception e) {
            System.out.println("Failed to find a valid main() method for class: " + mainClass);
            System.exit(1);
        }
        try {
            if (debug) System.out.printf("Invoking with %d args\n", args.length);
            m.invoke(null, (Object) args);
        } catch (Exception e) {
            System.out.println("Error Launching Class: " + mainClass);
            e.printStackTrace();
            System.exit(1);
        }
        if (debug) System.out.println("Command completed without error.");
        System.exit(0);
    }
