    private void outputSignedOpenDocument(byte[] signatureData) throws IOException {
        LOG.debug("output signed open document");
        OutputStream signedOdfOutputStream = getSignedOpenDocumentOutputStream();
        if (null == signedOdfOutputStream) {
            throw new NullPointerException("signedOpenDocumentOutputStream is null");
        }
        ZipOutputStream zipOutputStream = new ZipOutputStream(signedOdfOutputStream);
        ZipInputStream zipInputStream = new ZipInputStream(this.getOpenDocumentURL().openStream());
        ZipEntry zipEntry;
        while (null != (zipEntry = zipInputStream.getNextEntry())) {
            if (!zipEntry.getName().equals(ODFUtil.SIGNATURE_FILE)) {
                zipOutputStream.putNextEntry(zipEntry);
                IOUtils.copy(zipInputStream, zipOutputStream);
            }
        }
        zipInputStream.close();
        zipEntry = new ZipEntry(ODFUtil.SIGNATURE_FILE);
        zipOutputStream.putNextEntry(zipEntry);
        IOUtils.write(signatureData, zipOutputStream);
        zipOutputStream.close();
    }
