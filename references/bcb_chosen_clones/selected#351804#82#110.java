    protected void copyFile(String from, String to, String workingDirectory) throws Exception {
        URL monitorCallShellScriptUrl = Thread.currentThread().getContextClassLoader().getResource(from);
        File f = new File(monitorCallShellScriptUrl.getFile());
        String directoryPath = f.getAbsolutePath();
        String fileName = from;
        InputStream in = null;
        if (directoryPath.indexOf(".jar!") > -1) {
            URL urlJar = new URL(directoryPath.substring(directoryPath.indexOf("file:"), directoryPath.indexOf('!')));
            JarFile jf = new JarFile(urlJar.getFile());
            JarEntry je = jf.getJarEntry(from);
            fileName = je.getName();
            in = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        } else {
            in = new FileInputStream(f);
        }
        File outScriptFile = new File(to);
        FileOutputStream fos = new FileOutputStream(outScriptFile);
        int nextChar;
        while ((nextChar = in.read()) != -1) fos.write(nextChar);
        fos.flush();
        fos.close();
        try {
            LinuxCommandExecutor cmdExecutor = new LinuxCommandExecutor();
            cmdExecutor.setWorkingDirectory(workingDirectory);
            cmdExecutor.runCommand("chmod 777 " + to);
        } catch (Exception e) {
            throw e;
        }
    }
