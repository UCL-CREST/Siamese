    private void startRunner() throws IOException {
        String[] cmd = new String[] { (String.valueOf(new File(System.getProperty("java.home"), "bin" + File.separator + "java"))), "-jar", base + File.separator + "ext" + File.separator + "JavaBridge.jar", "SERVLET_LOCAL:" + socket };
        System.err.println("Starting a simple servlet engine: " + Arrays.asList(cmd));
        Process p;
        try {
            p = runner = Runtime.getRuntime().exec(cmd);
        } catch (java.io.IOException ex) {
            throw new RuntimeException("Could not run " + Arrays.asList(cmd) + ".", ex);
        }
        (new Thread(new TestInstallation(p))).start();
        InputStream i = p.getInputStream();
        int c;
        while ((c = i.read()) != -1) System.out.write(c);
        System.out.flush();
        System.err.flush();
    }
