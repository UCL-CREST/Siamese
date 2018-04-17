    public String getResourceAsString(String name) throws IOException {
        String content = null;
        InputStream stream = aClass.getResourceAsStream(name);
        if (stream != null) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            IOUtils.copyAndClose(stream, buffer);
            content = buffer.toString();
        } else {
            Assert.fail("Resource not available: " + name);
        }
        return content;
    }
