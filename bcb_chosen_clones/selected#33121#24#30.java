    public String getResource(String resourceName) throws IOException {
        InputStream resourceStream = resourceClass.getResourceAsStream(resourceName);
        ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);
        IOUtils.copyAndClose(resourceStream, baos);
        String expected = new String(baos.toByteArray());
        return expected;
    }
