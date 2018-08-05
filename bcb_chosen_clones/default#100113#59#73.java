    void init(String[] args) throws IOException, InterruptedException {
        String[] cmdArgs = new String[args.length + 1];
        cmdArgs[0] = new File(new File(jdk, "bin"), "javah").getPath();
        System.arraycopy(args, 0, cmdArgs, 1, args.length);
        System.out.println("init: " + Arrays.asList(cmdArgs));
        ProcessBuilder pb = new ProcessBuilder(cmdArgs);
        pb.directory(new File(testSrc, "gold"));
        pb.redirectErrorStream(true);
        Process p = pb.start();
        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) System.out.println("javah: " + line);
        int rc = p.waitFor();
        if (rc != 0) error("javah: exit code " + rc);
    }
