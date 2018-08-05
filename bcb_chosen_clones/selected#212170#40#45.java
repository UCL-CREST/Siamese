    public void addKmlFile(byte[] doc) throws IOException {
        ZipEntry zKml = new ZipEntry("data.kml");
        kmz.putNextEntry(zKml);
        kmz.write(doc);
        kmz.closeEntry();
    }
