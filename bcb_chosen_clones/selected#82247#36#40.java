    public void addXSLT(InputStream in) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        IOUtils.copy(in, bytes);
        xslt.add(0, bytes.toByteArray());
    }
