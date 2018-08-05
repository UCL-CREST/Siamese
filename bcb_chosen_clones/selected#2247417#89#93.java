    private void writeXMLToZipArchive(String filename, String xml, ZipOutputStream zout) throws Exception {
        ZipEntry entry = new ZipEntry(filename);
        zout.putNextEntry(entry);
        zout.write(xml.getBytes());
    }
