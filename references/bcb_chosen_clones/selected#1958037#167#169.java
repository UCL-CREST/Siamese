    public static void copy(InputStream stream, OutputStream ostream) throws IOException {
        IOUtils.copy(stream, ostream, false);
    }
