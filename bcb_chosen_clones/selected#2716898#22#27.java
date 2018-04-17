    public String convertContent(InputStream inputStream, String encoding) throws IOException {
        StringWriter writer = new StringWriter();
        InputStreamReader in = new InputStreamReader(inputStream, encoding);
        IOUtils.copy(in, writer);
        return writer.toString();
    }
