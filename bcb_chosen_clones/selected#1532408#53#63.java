    private void writeFile() throws IOException {
        ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(dest)));
        zipOut.putNextEntry(new ZipEntry("manifest.xml"));
        zipOut.write((MANIFEST_XML_1 + System.currentTimeMillis() + MANIFEST_XML_2).getBytes("UTF-8"));
        zipOut.closeEntry();
        zipOut.putNextEntry(new ZipEntry("message.xml"));
        zipOut.write(getXmlData().getBytes("UTF-8"));
        zipOut.closeEntry();
        zipOut.finish();
        zipOut.close();
    }
