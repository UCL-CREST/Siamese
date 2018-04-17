    public static byte[] loadFile(File file) throws IOException {
        BufferedInputStream in = null;
        ByteArrayOutputStream sink = null;
        try {
            in = new BufferedInputStream(new FileInputStream(file));
            sink = new ByteArrayOutputStream();
            IOUtils.copy(in, sink);
            return sink.toByteArray();
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(sink);
        }
    }
