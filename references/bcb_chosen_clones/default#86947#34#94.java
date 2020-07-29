    public static void start() {
        File workdir = new File(System.getProperty("user.dir"));
        File outputdir = new File(workdir, "output");
        if (!outputdir.exists()) {
            System.out.println("***********************************************************************");
            System.out.println("");
            System.out.println("  Directory " + outputdir + " does not exist.");
            System.out.println("");
            System.out.println("  Make sure you've built the native executable first before running ");
            System.out.println("  these tests.");
            System.out.println("");
            System.out.println("***********************************************************************");
            System.exit(-1);
        }
        String osname = System.getProperty("os.name");
        File iodaemon = new File("");
        if (osname.startsWith("Linux")) {
            throw new Error("TODO: IMPLEMENT LINUX IODAEMON PATH");
        } else if (osname.startsWith("Windows")) {
            iodaemon = new File(outputdir, "cygwin");
            iodaemon = new File(iodaemon, "iodaemon-" + VERSION + ".exe");
        } else if (osname.startsWith("Mac")) {
            throw new Error("TODO: IMPLEMENT MACOS IODAEMON PATH");
        }
        if (!iodaemon.exists()) {
            System.out.println("***********************************************************************");
            System.out.println("");
            System.out.println("  " + iodaemon.getAbsolutePath() + " does not exist.");
            System.out.println("");
            System.out.println("  Make sure you've built the native executable first before running ");
            System.out.println("  these tests.");
            System.out.println("");
            System.out.println("***********************************************************************");
            System.exit(-1);
        }
        ProcessBuilder builder = new ProcessBuilder(iodaemon.getAbsolutePath(), "--port=" + TEST_DAEMON_PORT);
        try {
            process = builder.start();
            readInputStream(process.getInputStream());
            readErrorStream(process.getErrorStream());
            try {
                int exitValue = process.exitValue();
                System.out.println("***********************************************************************");
                System.out.println("");
                System.out.println("  I/O Daemon did not start normally (exit value: " + exitValue + ").");
                System.out.println("");
                System.out.println("***********************************************************************");
                System.exit(-1);
            } catch (IllegalThreadStateException e) {
            }
        } catch (Exception e) {
            System.out.println("***********************************************************************");
            System.out.println("");
            System.out.println("  Cannot start I/O daemon!");
            System.out.println("");
            System.out.println("  " + e.toString());
            System.out.println("");
            System.out.println("***********************************************************************");
            System.exit(-1);
        }
    }
