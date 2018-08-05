    public static void addToZip(ZipOutputStream out, URL path, String omitPrefix, Logger logger) {
        byte[] buffer = new byte[1024];
        try {
            InputStream in = path.openStream();
            out.putNextEntry(new ZipEntry(URLTools.unescape(path.getPath().replace(omitPrefix, ""))));
            for (int length = 0; (length = in.read(buffer)) > 0; ) {
                out.write(buffer, 0, length);
            }
            out.closeEntry();
            in.close();
        } catch (IOException e) {
            if (logger != null && e instanceof ZipException) {
                logger.warn(e);
                return;
            }
            throw new RuntimeException("Error adding file to zip file.", e);
        }
    }
