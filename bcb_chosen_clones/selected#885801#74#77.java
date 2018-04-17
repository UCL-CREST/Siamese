    @Override
    public void writeTo(OutputStream outstream) throws IOException {
        IOUtils.copy(entry.getContent().getInputStream(), outstream);
    }
