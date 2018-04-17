    private static void addFileToZip(final String path, final File file, final ZipOutputStream zip) throws IOException {
        final byte[] buffer = new byte[1024];
        int read = -1;
        final FileInputStream in = new FileInputStream(file);
        try {
            final String zipEntry = path + File.separator + file.getName();
            LOG.debug("add zip entry: " + zipEntry);
            zip.putNextEntry(new ZipEntry(zipEntry));
            while ((read = in.read(buffer)) > -1) {
                zip.write(buffer, 0, read);
            }
        } finally {
            in.close();
        }
    }
