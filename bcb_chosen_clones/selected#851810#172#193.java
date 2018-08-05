    public String zip(String sourceFilePath) {
        byte[] buf = new byte[MEGA];
        try {
            File sourceFile = IOUtil.getExistentFile(sourceFilePath);
            String resultPath = sourceFilePath + ".zip";
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
            return resultPath;
        } catch (IOException ioe) {
            logger.error(UNHANDLED_EXCEPTION + ioe.getMessage());
            ioe.printStackTrace();
            throw new RuntimeException("Error while zipping file: " + sourceFilePath, ioe);
        }
    }
