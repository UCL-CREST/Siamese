    private ByteBuffer getByteBuffer(String resource) throws IOException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        InputStream in = classLoader.getResourceAsStream(resource);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IOUtils.copy(in, out);
        return ByteBuffer.wrap(out.toByteArray());
    }
