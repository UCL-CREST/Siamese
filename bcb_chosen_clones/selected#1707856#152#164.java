    void copyZipToZip(ZipInputStream zis, ZipOutputStream zos) throws Exception {
        ZipEntry entry;
        byte[] buffer = new byte[1];
        while ((entry = zis.getNextEntry()) != null) {
            zos.putNextEntry(new ZipEntry(entry.getName()));
            while (zis.available() == 1) {
                zis.read(buffer, 0, buffer.length);
                zos.write(buffer, 0, buffer.length);
            }
            zis.closeEntry();
            zos.closeEntry();
        }
    }
