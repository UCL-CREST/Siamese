    private void writeFileToZip(ZipOutputStream zipOut, byte[] bytes, String fileName) throws IOException {
        ZipEntry entry = new ZipEntry(fileName);
        zipOut.putNextEntry(entry);
        zipOut.write(bytes, 0, bytes.length);
        zipOut.closeEntry();
    }
