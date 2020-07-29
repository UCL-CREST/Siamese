    public static InputStream gunzip(final InputStream inputStream) throws IOException {
        Assert.notNull(inputStream, "inputStream");
        GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);
        InputOutputStream inputOutputStream = new InputOutputStream();
        IOUtils.copy(gzipInputStream, inputOutputStream);
        return inputOutputStream.getInputStream();
    }
