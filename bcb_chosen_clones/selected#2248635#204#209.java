    private String getResourceAsString(final String name) throws IOException {
        final InputStream is = JiBXTestCase.class.getResourceAsStream(name);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IOUtils.copyAndClose(is, baos);
        return baos.toString();
    }
