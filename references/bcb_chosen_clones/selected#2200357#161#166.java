    private byte[] getBytes(String resource) throws IOException {
        InputStream is = ResourceFilterTest.class.getResourceAsStream(resource);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IOUtils.copy(is, out);
        return out.toByteArray();
    }
