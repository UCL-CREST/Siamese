    private static byte[] getFileContents(@NotNull final File file) throws IOException {
        final ByteArrayOutputStream bufOut = new ByteArrayOutputStream((int) file.length());
        final InputStream inputStream = new FileInputStream(file);
        try {
            final byte[] buf = new byte[CHUNK_SIZE];
            while (true) {
                final int len = inputStream.read(buf);
                if (len == -1) {
                    break;
                }
                bufOut.write(buf, 0, len);
            }
        } finally {
            inputStream.close();
        }
        return bufOut.toByteArray();
    }
