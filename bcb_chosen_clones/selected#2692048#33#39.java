    public TempFileBinaryBody(InputStream is) throws IOException {
        TempPath tempPath = TempStorage.getInstance().getRootTempPath();
        tempFile = tempPath.createTempFile("attachment", ".bin");
        OutputStream out = tempFile.getOutputStream();
        IOUtils.copy(is, out);
        out.close();
    }
