    private void writeData(IBaseType dataType, Writer writer) throws XMLStreamException {
        InputStream isData;
        DataType data = (DataType) baseType;
        if (data.isSetInputStream()) {
            isData = data.getInputStream();
            try {
                IOUtils.copy(isData, writer);
            } catch (IOException e) {
                throw new XMLStreamException("DataType fail writing streaming data ", e);
            }
        } else if (data.isSetOutputStream()) {
            throw new XMLStreamException("DataType only can write streaming input, its an output stream (only for reading) ");
        } else {
            new CharactersEventImpl(startElement.getLocation(), String.valueOf(baseType.asData()), false).writeAsEncodedUnicode(writer);
        }
    }
