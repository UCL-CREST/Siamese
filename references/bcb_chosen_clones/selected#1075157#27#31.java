    public InputStreamByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);
        IOUtils.copyAndClose(input, baos);
        buffer = baos.toByteArray();
    }
