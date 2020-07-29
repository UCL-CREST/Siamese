    public SlurpedByteArrayItem(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IOUtils.copy(is, baos);
        data = baos.toByteArray();
    }
