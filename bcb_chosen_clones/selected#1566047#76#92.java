    protected void zipFile(ZipOutputStream out, String file) {
        byte[] buffer = new byte[18024];
        try {
            logger.debug("Adding " + file + " to zip");
            FileInputStream in = new FileInputStream(file);
            out.putNextEntry(new ZipEntry(file));
            int len;
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            out.closeEntry();
            in.close();
        } catch (Exception e) {
            logger.error("Failed to add file " + file + " to zip!", e);
            throw new RuntimeException("Failed to add file " + file + " to zip!", e);
        }
    }
