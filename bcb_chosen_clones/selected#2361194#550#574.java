    private byte[] compile(File classesDirectory, String[] files) {
        String[] cmdarray = new String[files.length + 5];
        cmdarray[0] = "javaw";
        cmdarray[1] = "-jar";
        cmdarray[2] = DX_JAR_FILE_PATH;
        cmdarray[3] = "--dex";
        String dexFilePath = new File(SystemUtils.JAVA_IO_TMPDIR, FilenameUtils.getBaseName(files[0]) + ".dex").getAbsolutePath();
        cmdarray[4] = "--output=" + dexFilePath;
        System.arraycopy(files, 0, cmdarray, 5, files.length);
        try {
            Process process = Runtime.getRuntime().exec(cmdarray, null, classesDirectory);
            process.waitFor();
        } catch (Exception e) {
            throw new IllegalStateException("Compiling failed.", e);
        }
        FileInputStream in = null;
        try {
            in = new FileInputStream(dexFilePath);
            return IOUtils.toByteArray(in);
        } catch (IOException e) {
            throw new IllegalStateException("Loading " + dexFilePath + " failed.", e);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }
