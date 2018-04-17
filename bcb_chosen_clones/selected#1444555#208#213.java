    private void addFile(ZipOutputStream zos, String path, Element details, String name) throws Exception {
        ZipEntry entry = new ZipEntry(name);
        zos.putNextEntry(entry);
        BinaryFile.write(details, zos);
        zos.closeEntry();
    }
