    public static void write(File file, InputStream source) throws IOException {
        OutputStream outputStream = null;
        assert file != null : "file must not be null.";
        assert file.isFile() : "file must be a file.";
        assert file.canWrite() : "file must be writable.";
        assert source != null : "source must not be null.";
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(file));
            IOUtils.copy(source, outputStream);
            outputStream.flush();
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
    }
