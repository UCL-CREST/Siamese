    public void zip(String targetFilePath, String sourceDirPath) {
        byte[] buf = new byte[MEGA];
        try {
            File sourceDir = IOUtil.getExistentFile(sourceDirPath);
            if (!sourceDir.exists() || !sourceDir.isDirectory()) {
                throw new RemoteException("Invalid sourceDirPath: " + sourceDirPath);
            }
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(targetFilePath));
            File[] files = sourceDir.listFiles();
            logger.debug("Found: " + files.length + " files");
            for (int i = 0; i < files.length; i++) {
                FileInputStream in = new FileInputStream(files[i]);
                out.putNextEntry(new ZipEntry(files[i].getName()));
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
            out.close();
        } catch (IOException ioe) {
            logger.error(UNHANDLED_EXCEPTION + ioe.getMessage());
            ioe.printStackTrace();
            throw new RuntimeException("Error while zipping files from: " + sourceDirPath, ioe);
        }
    }
