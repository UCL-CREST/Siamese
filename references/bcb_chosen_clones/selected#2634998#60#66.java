    private static void copyAll(Reader in, Writer out) {
        try {
            IOUtils.copy(in, out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
