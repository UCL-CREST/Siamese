    private static void addEntry(final ZipOutputStream zos, final File file, final int bufferSize) throws IOException {
        if (log.isTraceEnabled()) log.trace(HelperLog.methodStart(zos, file, bufferSize));
        BufferedInputStream bis = null;
        final byte[] buffer = new byte[bufferSize];
        try {
            final ZipEntry entry = new ZipEntry(file.getPath() + (file.isDirectory() ? "/" : HelperString.EMPTY_STRING));
            zos.putNextEntry(entry);
            if (!file.isDirectory()) {
                bis = new BufferedInputStream(new FileInputStream(file));
                int offset;
                while (-1 != (offset = bis.read(buffer))) {
                    zos.write(buffer, 0, offset);
                }
            }
        } finally {
            if (null != bis) {
                bis.close();
            }
        }
        if (log.isTraceEnabled()) log.trace(HelperLog.methodExit());
    }
