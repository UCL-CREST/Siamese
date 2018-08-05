    public static void resourceToFile(final String resource, final String filePath) throws IOException {
        log.debug("Classloader is " + IOCopyUtils.class.getClassLoader());
        InputStream in = IOCopyUtils.class.getResourceAsStream(resource);
        if (in == null) {
            log.warn("Resource not '" + resource + "' found. Try to prefix with '/'");
            in = IOCopyUtils.class.getResourceAsStream("/" + resource);
        }
        if (in == null) {
            throw new IOException("Resource not '" + resource + "' found.");
        }
        final File file = new File(filePath);
        final OutputStream out = FileUtils.openOutputStream(file);
        final int bytes = IOUtils.copy(in, out);
        IOUtils.closeQuietly(out);
        IOUtils.closeQuietly(in);
        log.debug("Copied resource '" + resource + "' to file " + filePath + " (" + bytes + " bytes)");
    }
