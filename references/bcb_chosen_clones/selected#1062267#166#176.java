    public static java.io.ByteArrayOutputStream getFileByteStream(URL _url) {
        java.io.ByteArrayOutputStream buffer = new java.io.ByteArrayOutputStream();
        try {
            InputStream input = _url.openStream();
            IOUtils.copy(input, buffer);
            IOUtils.closeQuietly(input);
        } catch (Exception err) {
            throw new RuntimeException(err);
        }
        return buffer;
    }
