    public static final void zipDirectory(String zipFileName, File baseDir, File directory) throws IOException {
        if (directory == null || zipFileName == null) {
            throw new IllegalArgumentException("Directory or zip file name was null");
        }
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory + " is not a directory");
        }
        if (!directory.exists()) {
            throw new IllegalArgumentException(directory + " does not exist");
        }
        List<String> allDescendentFiles = FileUtils.getFilesInDir(directory, null);
        byte[] buffer = new byte[4096];
        int bytesRead;
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
        for (String fileEntry : allDescendentFiles) {
            File f = new File(fileEntry);
            if (f.isDirectory()) {
                continue;
            }
            FileInputStream in = new FileInputStream(f);
            String localPath = baseDir != null ? f.getAbsolutePath().substring(baseDir.getAbsolutePath().length() + 1) : f.getPath();
            Logger.debug(LOG_INSTANCE, "Basedir   : " + baseDir.getPath());
            Logger.debug(LOG_INSTANCE, "File path : " + f.getPath());
            Logger.debug(LOG_INSTANCE, "Local path: " + localPath);
            ZipEntry entry = new ZipEntry(localPath);
            out.putNextEntry(entry);
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            in.close();
        }
        out.close();
    }
