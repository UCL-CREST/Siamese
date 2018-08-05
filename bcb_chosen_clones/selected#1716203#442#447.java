    public void addZipEntry(ZipOutputStream zip, String zipName, File zipFile) throws IOException {
        ZipEntry entry = new ZipEntry(zipName);
        zip.putNextEntry(entry);
        if (!zipFile.isDirectory()) zip.write(getFileContents(zipFile));
        zip.closeEntry();
    }
