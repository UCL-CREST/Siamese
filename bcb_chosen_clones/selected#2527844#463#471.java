    public static long writeInputStreamToOutputStream(final InputStream in, final OutputStream out) {
        long size = 0;
        try {
            size = IOUtils.copyLarge(in, out);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return size;
    }
