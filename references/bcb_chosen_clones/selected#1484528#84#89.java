    @Override
    protected Void produce(final OutputStream sink) throws Exception {
        IOUtils.copy(this.source, sink);
        this.source.close();
        return null;
    }
