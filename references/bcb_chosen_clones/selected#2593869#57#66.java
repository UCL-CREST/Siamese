    protected void startDocument() throws XMLStreamException, IOException {
        this.zipStream.putNextEntry(new ZipEntry(this.getMainFileName()));
        this.writer = XMLOutputFactory.newInstance().createXMLStreamWriter(this.zipStream);
        this.writer.writeStartDocument();
        this.writer.writeStartElement("kml");
        this.writer.writeDefaultNamespace(KMLConstants.KML_NAMESPACE);
        this.writer.setPrefix("gx", GXConstants.GX_NAMESPACE);
        this.writer.writeNamespace("gx", GXConstants.GX_NAMESPACE);
        this.writer.writeStartElement("Document");
    }
