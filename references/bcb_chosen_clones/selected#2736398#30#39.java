    @Override
    public Document duplicate() {
        BinaryDocument b = new BinaryDocument(this.name, this.content.getContentType());
        try {
            IOUtils.copy(this.getContent().getInputStream(), this.getContent().getOutputStream());
            return b;
        } catch (IOException e) {
            throw ManagedIOException.manage(e);
        }
    }
