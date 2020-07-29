    private void write(Path path, InputStream in) throws IOException {
        FSDataOutputStream out = fileSystem.create(path, true);
        try {
            IOUtils.copy(in, out);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }
