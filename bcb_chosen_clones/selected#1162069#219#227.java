    private byte[] streamToBytes(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            IOUtils.copy(in, out);
        } finally {
            IOUtils.closeQuietly(in);
        }
        return out.toByteArray();
    }
