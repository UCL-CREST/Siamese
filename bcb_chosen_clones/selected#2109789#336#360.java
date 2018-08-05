    private String zip(String sourceFilePath) {
        byte[] buf = new byte[MEGA];
        debug("Zipping " + sourceFilePath);
        try {
            File sourceFile = IOUtil.getExistentFile(sourceFilePath);
            String resultPath = FilenameUtils.concat(FilenameUtils.getFullPath(sourceFilePath), FilenameUtils.getBaseName(sourceFilePath) + ".zip");
            debug("Result will be " + resultPath);
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
            debug("Zip operation succeeded.");
            return resultPath;
        } catch (IOException ioe) {
            logger.error("Unhandled exception: " + ioe.getMessage());
            ioe.printStackTrace();
            throw new RuntimeException("Error while zipping file: " + sourceFilePath, ioe);
        }
    }
