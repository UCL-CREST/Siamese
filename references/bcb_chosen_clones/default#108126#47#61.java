    void javap(PrintWriter out, String... args) throws Exception {
        File javaHome = new File(System.getProperty("java.home"));
        if (javaHome.getName().equals("jre")) javaHome = javaHome.getParentFile();
        File javap = new File(new File(javaHome, "bin"), "javap");
        String[] cmd = new String[args.length + 1];
        cmd[0] = javap.getPath();
        System.arraycopy(args, 0, cmd, 1, args.length);
        Process p = new ProcessBuilder(cmd).redirectErrorStream(true).start();
        p.getOutputStream().close();
        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) out.println(line);
        int rc = p.waitFor();
        if (rc != 0) throw new Error("javap failed: " + Arrays.asList(args) + ": " + rc);
    }
