    public Object read(InputStream inputStream, Map metadata) throws IOException, ClassNotFoundException {
        if (log.isTraceEnabled()) log.trace("Read input stream with metadata=" + metadata);
        Integer resCode = (Integer) metadata.get(HTTPMetadataConstants.RESPONSE_CODE);
        String resMessage = (String) metadata.get(HTTPMetadataConstants.RESPONSE_CODE_MESSAGE);
        if (resCode != null && validResponseCodes.contains(resCode) == false) throw new RuntimeException("Invalid HTTP server response [" + resCode + "] - " + resMessage);
        ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
        IOUtils.copyStream(baos, inputStream);
        String soapMessage = new String(baos.toByteArray(), charsetEncoding);
        if (isTraceEnabled) {
            String prettySoapMessage = DOMWriter.printNode(DOMUtils.parse(soapMessage), true);
            log.trace("Incoming Response SOAPMessage\n" + prettySoapMessage);
        }
        return soapMessage;
    }
