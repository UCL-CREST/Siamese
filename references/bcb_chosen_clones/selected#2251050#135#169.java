    private void processJobFile(final IFile iJobFile) throws IOException, CoreException, InterruptedException {
        final String java = System.getProperty("java.home") + "/bin/java";
        final IProject globalProject = NofdpCorePlugin.getProjectManager().getBaseProject();
        final IFolder ooLibFolder = globalProject.getFolder(NofdpIDSSConstants.NOFDP_PROJECT_REPORTING_OO_LIB_FOLDER_PATH);
        if (!ooLibFolder.exists()) throw new IllegalStateException(Messages.OpenOfficeExporter_6);
        final File ooJarFile = ooLibFolder.getFile("ooreporting.jar").getLocation().toFile();
        final File log4jFile = ooLibFolder.getFile("log4j.conf").getLocation().toFile();
        if (!log4jFile.exists()) throw new IllegalStateException("log configuration file has to exist!");
        final String ooJarString = ooJarFile.toString();
        final String log4jString = log4jFile.toString();
        final String reportingDataString = iJobFile.getLocation().toFile().toString();
        final String[] cmd = new String[] { java, "-jar", "\"" + ooJarString + "\"", "-c", "\"" + log4jString + "\"", "-f", "\"" + reportingDataString + "\"" };
        final Process exec = Runtime.getRuntime().exec(cmd, null, iJobFile.getLocation().toFile().getParentFile());
        final InputStream errorStream = exec.getErrorStream();
        final InputStream inputStream = exec.getInputStream();
        final StreamGobbler error = new StreamGobbler(errorStream, "Report: ERROR_STREAM");
        final StreamGobbler input = new StreamGobbler(inputStream, "Report: INPUT_STREAM");
        error.start();
        input.start();
        int exitValue = 0;
        int timeRunning = 0;
        while (true) {
            try {
                exitValue = exec.exitValue();
                break;
            } catch (final RuntimeException e) {
            }
            if (timeRunning >= 300000) {
                exec.destroy();
                throw new CoreException(StatusUtilities.createErrorStatus(Messages.OpenOfficeExporter_21));
            }
            Thread.sleep(100);
            timeRunning = timeRunning + 100;
        }
    }
