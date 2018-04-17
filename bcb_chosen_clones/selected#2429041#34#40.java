    public MemoryBinaryBody(InputStream is) throws IOException {
        TempPath tempPath = TempStorage.getInstance().getRootTempPath();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IOUtils.copy(is, out);
        out.close();
        tempFile = out.toByteArray();
    }
