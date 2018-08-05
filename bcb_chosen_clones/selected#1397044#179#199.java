    public static int save(byte[] bytes, File outputFile) throws IOException {
        InputStream in = new ByteArrayInputStream(bytes);
        outputFile.getParentFile().mkdirs();
        OutputStream out = new FileOutputStream(outputFile);
        try {
            return IOUtils.copy(in, out);
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
            try {
                out.close();
            } catch (IOException ioe) {
                ioe.getMessage();
            }
            try {
                in.close();
            } catch (IOException ioe) {
                ioe.getMessage();
            }
        }
    }
