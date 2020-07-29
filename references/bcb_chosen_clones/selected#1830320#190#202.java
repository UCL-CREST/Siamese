    protected static void writeFileToStream(FileWrapper file, String filename, ZipOutputStream zipStream) throws CausedIOException, IOException {
        InputStream in;
        try {
            in = file.getInputStream();
        } catch (Exception e) {
            throw new CausedIOException("Could not obtain InputStream for " + filename, e);
        }
        try {
            IOUtils.copy(in, zipStream);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }
