    public static byte[] readAll(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IOUtils.copyStream(is, baos);
        is.close();
        return baos.toByteArray();
    }
