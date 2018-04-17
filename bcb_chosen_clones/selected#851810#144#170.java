    public File zip(File sourceFile) {
        if (sourceFile == null || !sourceFile.exists()) {
            throw new IllegalArgumentException("File does not exist: " + sourceFile);
        }
        if (FilenameUtils.getExtension(sourceFile.getAbsolutePath()).equalsIgnoreCase("zip")) {
            return sourceFile;
        }
        byte[] buf = new byte[MEGA];
        try {
            String resultPath = sourceFile.getAbsolutePath() + ".zip";
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(resultPath));
            FileInputStream in = new FileInputStream(sourceFile);
            out.putNextEntry(new ZipEntry(sourceFile.getName()));
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.closeEntry();
            in.close();
            out.close();
            return new File(resultPath);
        } catch (Exception ioe) {
            logger.error("Error while zipping file: " + ioe.getMessage());
            ioe.printStackTrace();
            throw new RuntimeException("Error while zipping file: " + sourceFile, ioe);
        }
    }
