    public void addZipEntry(ZipOutputStream zip, String zipName, byte[] _bytes) throws IOException {
        ZipEntry entry = new ZipEntry(zipName);
        zip.putNextEntry(entry);
        if (_bytes != null) zip.write(_bytes);
        zip.closeEntry();
    }
