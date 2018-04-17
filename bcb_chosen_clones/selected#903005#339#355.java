    protected void writeManifestPropertiesZipEntry(ZipOutputStream zipStream) throws IOException {
        ZipEntry zipEntry = new ZipEntry(META_INF_DIR + "manifest.properties");
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Writer w = new OutputStreamWriter(bout, "ISO-8859-1");
        writeManifestProperties(w);
        w.close();
        bout.close();
        byte[] manifestProperties = bout.toByteArray();
        zipEntry.setMethod(ZipEntry.STORED);
        zipEntry.setSize(manifestProperties.length);
        CRC32 crc = new CRC32();
        crc.update(manifestProperties);
        zipEntry.setCrc(crc.getValue());
        zipStream.putNextEntry(zipEntry);
        zipStream.write(manifestProperties);
        zipStream.closeEntry();
    }
