    public void addXSLT(InputStream in) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IOUtils.copy(in, baos);
        transformers.addFirst(baos.toByteArray());
    }
