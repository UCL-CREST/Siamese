    public static DownloadedContent downloadContent(final InputStream is) throws IOException {
        if (is == null) {
            return new DownloadedContent.InMemory(new byte[] {});
        }
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final byte[] buffer = new byte[1024];
        int nbRead;
        try {
            while ((nbRead = is.read(buffer)) != -1) {
                bos.write(buffer, 0, nbRead);
                if (bos.size() > MAX_IN_MEMORY) {
                    final File file = File.createTempFile("htmlunit", ".tmp");
                    file.deleteOnExit();
                    final FileOutputStream fos = new FileOutputStream(file);
                    bos.writeTo(fos);
                    IOUtils.copyLarge(is, fos);
                    fos.close();
                    return new DownloadedContent.OnFile(file);
                }
            }
        } finally {
            IOUtils.closeQuietly(is);
        }
        return new DownloadedContent.InMemory(bos.toByteArray());
    }
