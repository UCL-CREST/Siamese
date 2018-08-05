    public void setContentAsStream(final InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            IOUtils.copy(input, output);
        } finally {
            output.close();
        }
        this.content = output.toByteArray();
    }
