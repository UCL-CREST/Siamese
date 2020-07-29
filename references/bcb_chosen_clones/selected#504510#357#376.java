    private void startInline(Attributes attributes) throws URISyntaxException, DIDLSerializationException {
        String mimeType = attributes.getValue("mimeType");
        String encoding = attributes.getValue("encoding");
        String[] contentEncoding = attributes.getValue("contentEncoding") == null ? null : attributes.getValue("contentEncoding").split("\\s+");
        URI ref = attributes.getValue("ref") == null ? null : new URI(attributes.getValue("ref"));
        fields = new Fields();
        fields.mimeType = mimeType;
        fields.encoding = encoding;
        fields.contentEncoding = contentEncoding;
        fields.ref = ref;
        inline = true;
        inlineBuffer = new ByteArrayOutputStream();
        buffer = new PrintWriter(inlineBuffer);
        try {
            Constructor c = copierClass.getConstructor(new Class[] { Writer.class });
            copier = (DefaultHandler2) (c.newInstance(buffer));
        } catch (Exception ex) {
            throw new DIDLSerializationException(ex);
        }
    }
