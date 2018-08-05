    void exportCoveritem(final XMLStreamWriter writer, final ZipOutputStream out, final String type, final byte[] data, final Integer id) throws XMLStreamException, IOException {
        writer.writeStartElement(TypeConstants.XML_COVERITEM);
        writer.writeAttribute("id", id.toString());
        writer.writeAttribute("type", type);
        final String filename = getRandomFilename();
        out.putNextEntry(new ZipEntry(filename));
        IOUtils.write(data, out);
        out.closeEntry();
        writer.writeAttribute("filename", filename);
        _usedFilenames.add(filename);
        writer.writeEndElement();
    }
