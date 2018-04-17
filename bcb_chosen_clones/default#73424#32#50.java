    void testCommand(String[] args, int expect_rc) throws Exception {
        System.err.println("Test command: " + Arrays.asList(args));
        File javaHome = new File(System.getProperty("java.home"));
        if (javaHome.getName().equals("jre")) javaHome = javaHome.getParentFile();
        List<String> command = new ArrayList<String>();
        command.add(new File(new File(javaHome, "bin"), "javah").getPath());
        command.add("-J-Xbootclasspath:" + System.getProperty("sun.boot.class.path"));
        command.addAll(Arrays.asList(args));
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);
        Process p = pb.start();
        p.getOutputStream().close();
        StringWriter sw = new StringWriter();
        String line;
        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        while ((line = in.readLine()) != null) sw.write(line + NEWLINE);
        int rc = p.waitFor();
        expect("testCommand", sw.toString(), rc, expect_rc);
    }
