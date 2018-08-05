    public static void saveZipComponents(ZipComponents zipComponents, File zipFile) throws FileNotFoundException, IOException, Exception {
        ZipOutputStream zipOutStream = new ZipOutputStream(new FileOutputStream(zipFile));
        for (ZipComponent comp : zipComponents.getComponents()) {
            ZipEntry newEntry = new ZipEntry(comp.getName());
            zipOutStream.putNextEntry(newEntry);
            if (comp.isDirectory()) {
            } else {
                if (comp.getName().endsWith("document.xml") || comp.getName().endsWith("document.xml.rels")) {
                }
                InputStream inputStream = comp.getInputStream();
                IOUtils.copy(inputStream, zipOutStream);
                inputStream.close();
            }
        }
        zipOutStream.close();
    }
