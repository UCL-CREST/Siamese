    public static void copyAll(InputStream in, OutputStream out) {
        try {
            IOUtils.copy(in, out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
