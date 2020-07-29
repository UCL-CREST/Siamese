    private void addFile(ZipOutputStream zos, String name, InputStream in) throws IOException {
        ZipEntry entry = new ZipEntry(name);
        zos.putNextEntry(entry);
        BinaryFile.copy(in, zos, true, false);
        zos.closeEntry();
    }
