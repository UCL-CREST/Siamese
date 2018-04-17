    public static byte[] getStreamBytes(final InputStream in) throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        IOUtils.copyStream(in, out);
        return out.toByteArray();
    }
