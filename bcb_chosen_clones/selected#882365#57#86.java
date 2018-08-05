    public static File createGzip(File inputFile) {
        File targetFile = new File(inputFile.getParentFile(), inputFile.getName() + ".gz");
        if (targetFile.exists()) {
            log.warn("The target file '" + targetFile + "' already exists. Will overwrite");
        }
        FileInputStream in = null;
        GZIPOutputStream out = null;
        try {
            int read = 0;
            byte[] data = new byte[BUFFER_SIZE];
            in = new FileInputStream(inputFile);
            out = new GZIPOutputStream(new FileOutputStream(targetFile));
            while ((read = in.read(data, 0, BUFFER_SIZE)) != -1) {
                out.write(data, 0, read);
            }
            in.close();
            out.close();
            boolean deleteSuccess = inputFile.delete();
            if (!deleteSuccess) {
                log.warn("Could not delete file '" + inputFile + "'");
            }
            log.info("Successfully created gzip file '" + targetFile + "'.");
        } catch (Exception e) {
            log.error("Exception while creating GZIP.", e);
        } finally {
            StreamUtil.tryCloseStream(in);
            StreamUtil.tryCloseStream(out);
        }
        return targetFile;
    }
