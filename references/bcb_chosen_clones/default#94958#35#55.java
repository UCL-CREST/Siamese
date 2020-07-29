    int runJavah() throws Exception {
        List<String> cmd = new ArrayList<String>();
        File java_home = new File(System.getProperty("java.home"));
        if (java_home.getName().equals("jre")) java_home = java_home.getParentFile();
        cmd.add(new File(new File(java_home, "bin"), "javah").getPath());
        cmd.add("-J-Xbootclasspath:" + System.getProperty("sun.boot.class.path"));
        cmd.add("JavahTest");
        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.redirectErrorStream(true);
        pb.environment().remove("CLASSPATH");
        Process p = pb.start();
        p.getOutputStream().close();
        String line;
        DataInputStream in = new DataInputStream(p.getInputStream());
        try {
            while ((line = in.readLine()) != null) System.err.println(line);
        } finally {
            in.close();
        }
        return p.waitFor();
    }
