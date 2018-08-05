    private void outputSignedOfficeOpenXMLDocument(byte[] signatureData) throws IOException, ParserConfigurationException, SAXException, TransformerException {
        LOG.debug("output signed Office OpenXML document");
        OutputStream signedOOXMLOutputStream = getSignedOfficeOpenXMLDocumentOutputStream();
        if (null == signedOOXMLOutputStream) {
            throw new NullPointerException("signedOOXMLOutputStream is null");
        }
        String signatureZipEntryName = "_xmlsignatures/sig-" + UUID.randomUUID().toString() + ".xml";
        LOG.debug("signature ZIP entry name: " + signatureZipEntryName);
        ZipOutputStream zipOutputStream = copyOOXMLContent(signatureZipEntryName, signedOOXMLOutputStream);
        ZipEntry zipEntry = new ZipEntry(signatureZipEntryName);
        zipOutputStream.putNextEntry(zipEntry);
        IOUtils.write(signatureData, zipOutputStream);
        zipOutputStream.close();
    }
