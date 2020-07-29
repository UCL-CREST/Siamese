    public void writeToStream(OutputStream out) throws IOException {
        InputStream result = null;
        if (tempFile != null) {
            InputStream input = new BufferedInputStream(new FileInputStream(tempFile));
            IOUtils.copy(input, out);
            IOUtils.closeQuietly(input);
        } else if (tempBuffer != null) {
            out.write(tempBuffer);
        }
    }
