    @Override
    public String getPath() {
        InputStream in = null;
        OutputStream out = null;
        File file = null;
        try {
            file = File.createTempFile("java-storage_" + RandomStringUtils.randomAlphanumeric(32), ".tmp");
            file.deleteOnExit();
            out = new FileOutputStream(file);
            in = openStream();
            IOUtils.copy(in, out);
        } catch (IOException e) {
            throw new RuntimeException();
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
        if (file != null && file.exists()) {
            return file.getPath();
        }
        return null;
    }
