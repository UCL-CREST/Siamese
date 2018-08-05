    public static void copy(InputStream input, OutputStream output) throws IOException {
        IOUtils.copy(input, output, true);
    }
