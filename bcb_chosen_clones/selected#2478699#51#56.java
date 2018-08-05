    public static byte[] toByteArray(final InputStream inputStream) throws IOException {
        Assert.notNull(inputStream, "inputStream");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        IOUtils.copy(inputStream, outputStream);
        return outputStream.toByteArray();
    }
