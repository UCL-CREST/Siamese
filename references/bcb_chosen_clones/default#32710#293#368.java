    public static void bootstrap(String[] args) {
        System.out.println("JRIBootstrap(" + args + ")");
        try {
            System.loadLibrary("boot");
        } catch (Exception e) {
            fail("Unable to load boot library!");
        }
        String rhome = findR(true);
        if (rhome == null) fail("Unable to find R!");
        if (isWin32) {
            String path = getenv("PATH");
            if (path == null || path.length() < 1) path = rhome + "\\bin"; else path = rhome + "\\bin;" + path;
            setenv("PATH", path);
        }
        setREnv();
        System.out.println("PATH=" + getenv("PATH") + "\nR_LIBS=" + getenv("R_LIBS"));
        if (!isMac && !isWin32) {
            String stage = System.getProperty("stage");
            if (stage == null || stage.length() < 1) {
                File jl = new File(u2w(System.getProperty("java.home") + "/bin/java"));
                if (jl.exists()) {
                    try {
                        System.out.println(jl.toString() + " -cp " + System.getProperty("java.class.path") + " -Xmx512m -Dstage=2 Boot");
                        Process p = Runtime.getRuntime().exec(new String[] { jl.toString(), "-cp", System.getProperty("java.class.path"), "-Xmx512m", "-Dstage=2", "Boot" });
                        System.out.println("Started stage 2 (" + p + "), waiting for it to finish...");
                        System.exit(p.waitFor());
                    } catch (Exception re) {
                    }
                }
            }
        }
        String needPkg = null;
        String rj = findPackage("rJava");
        if (rj == null) {
            System.err.println("**ERROR: rJava is not installed");
            if (needPkg == null) needPkg = "'rJava'"; else needPkg += ",'rJava'";
        }
        String ipl = findPackage("iplots");
        if (ipl == null) {
            System.err.println("**ERROR: iplots is not installed");
            if (needPkg == null) needPkg = "'iplots'"; else needPkg += ",'iplots'";
        }
        String jgr = findPackage("JGR");
        if (jgr == null) {
            System.err.println("**ERROR: JGR is not installed");
            if (needPkg == null) needPkg = "'JGR'"; else needPkg += ",'JGR'";
        }
        if (needPkg != null) {
            if (!isWin32 && !isMac) {
                System.err.println("*** Please run the following in R as root to install missing packages:\n install.packages(c(" + needPkg + "),,'http://www.rforge.net/')");
                System.exit(4);
            }
            if (execR("install.packages(c(" + needPkg + "),,c('http://www.rforge.net/','http://cran.r-project.org'))") != 0) {
                System.err.println("*** ERROR: failed to install necessary packages");
                System.exit(4);
            }
            rj = findPackage("rJava");
            ipl = findPackage("iplots");
            jgr = findPackage("JGR");
            if (rj == null || ipl == null || jgr == null) {
                System.err.println("*** ERROR: failed to find installed packages");
                System.exit(5);
            }
        }
        Object o = bootRJavaLoader = createRJavaLoader(rhome, new String[] { "main" }, true);
        addClassPath(u2w(jgr + "/cont/JGR.jar"));
        addClassPath(u2w(ipl + "/cont/iplots.jar"));
        String mainClass = "org.rosuda.JGR.JGR";
        try {
            Method m = o.getClass().getMethod("bootClass", new Class[] { String.class, String.class, String[].class });
            m.invoke(o, new Object[] { mainClass, "main", args });
        } catch (Exception ie) {
            System.out.println("cannot boot the final class: " + ie);
            ie.printStackTrace();
        }
    }
