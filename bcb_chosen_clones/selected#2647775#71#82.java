    public static void writeToFile(InputStream input, File file, ProgressListener listener, long length) {
        OutputStream output = null;
        try {
            output = new CountingOutputStream(new FileOutputStream(file), listener, length);
            IOUtils.copy(input, output);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(output);
        }
    }
