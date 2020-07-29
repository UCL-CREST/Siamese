    private void copyFile(String from, String to) throws Exception {
        URL monitorCallShellScriptUrl = Thread.currentThread().getContextClassLoader().getResource(from);
        File inScriptFile = null;
        try {
            inScriptFile = new File(monitorCallShellScriptUrl.toURI());
        } catch (URISyntaxException e) {
            throw e;
        }
        File outScriptFile = new File(to);
        FileChannel inChannel = new FileInputStream(inScriptFile).getChannel();
        FileChannel outChannel = new FileOutputStream(outScriptFile).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } catch (IOException e) {
            throw e;
        } finally {
            if (inChannel != null) inChannel.close();
            if (outChannel != null) outChannel.close();
        }
        try {
            LinuxCommandExecutor cmdExecutor = new LinuxCommandExecutor();
            cmdExecutor.setWorkingDirectory(workingDirectory);
            cmdExecutor.runCommand("chmod 777 " + to);
        } catch (Exception e) {
            throw e;
        }
    }
