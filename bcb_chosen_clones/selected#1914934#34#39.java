    @Override
    public void writeTo(OutputStream out) throws IOException {
        OutputStream compressed = new GZIPOutputStream(out);
        IOUtils.copy(wrappedEntity.getContent(), compressed);
        compressed.close();
    }
