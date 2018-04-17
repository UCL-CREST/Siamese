    void run() throws Exception {
        File javaHome = new File(System.getProperty("java.home"));
        if (javaHome.getName().equals("jre")) javaHome = javaHome.getParentFile();
        File javadoc = new File(new File(javaHome, "bin"), "javadoc");
        File testSrc = new File(System.getProperty("test.src"));
        String thisClassName = TestStdDoclet.class.getName();
        Process p = new ProcessBuilder().command(javadoc.getPath(), "-J-Xbootclasspath:" + System.getProperty("sun.boot.class.path"), "-package", new File(testSrc, thisClassName + ".java").getPath()).redirectErrorStream(true).start();
        int actualDocletWarnCount = 0;
        int reportedDocletWarnCount = 0;
        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        try {
            String line;
            while ((line = in.readLine()) != null) {
                System.err.println(line);
                if (line.contains("DoesNotExist")) actualDocletWarnCount++;
                if (line.matches("[0-9]+ warning(s)?")) reportedDocletWarnCount = Integer.valueOf(line.substring(0, line.indexOf(" ")));
            }
        } finally {
            in.close();
        }
        int rc = p.waitFor();
        if (rc != 0) System.err.println("javadoc failed, rc:" + rc);
        int expectedDocletWarnCount = 2;
        checkEqual("actual", actualDocletWarnCount, "expected", expectedDocletWarnCount);
        checkEqual("actual", actualDocletWarnCount, "reported", reportedDocletWarnCount);
    }
