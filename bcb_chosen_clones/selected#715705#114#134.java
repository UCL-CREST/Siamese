    private static void writeZipEntry(final ZipOutputStream target, final String name, final InputStream source) throws IOException {
        final ZipEntry entry = new ZipEntry(name);
        target.putNextEntry(entry);
        try {
            synchronized (bufferLock) {
                buffer = new byte[1024 * 1024];
                try {
                    int bytes = source.read(buffer);
                    while (-1 != bytes) {
                        target.write(buffer, 0, bytes);
                        target.flush();
                        bytes = source.read(buffer);
                    }
                } finally {
                    buffer = null;
                }
            }
        } finally {
            target.closeEntry();
        }
    }
