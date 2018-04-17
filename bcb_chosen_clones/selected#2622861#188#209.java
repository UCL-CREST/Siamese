    private static void copyContent(final File srcFile, final File dstFile, final boolean gzipContent) throws IOException {
        final File dstFolder = dstFile.getParentFile();
        dstFolder.mkdirs();
        if (!dstFolder.exists()) {
            throw new RuntimeException("Unable to create the folder " + dstFolder.getAbsolutePath());
        }
        final InputStream in = new FileInputStream(srcFile);
        OutputStream out = new FileOutputStream(dstFile);
        if (gzipContent) {
            out = new GZIPOutputStream(out);
        }
        try {
            final byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
        } finally {
            in.close();
            out.close();
        }
    }
