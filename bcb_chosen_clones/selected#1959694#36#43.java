    public MemoryTextBody(InputStream is, String mimeCharset) throws IOException {
        this.mimeCharset = mimeCharset;
        TempPath tempPath = TempStorage.getInstance().getRootTempPath();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IOUtils.copy(is, out);
        out.close();
        tempFile = out.toByteArray();
    }
