    public void writeTo(OutputStream out) throws IOException {
        IOUtils.copy(tempFile.getInputStream(), out);
    }
