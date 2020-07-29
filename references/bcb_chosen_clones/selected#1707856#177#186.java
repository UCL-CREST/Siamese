    void copyResourceToZip(String entryName, InputStream is, ZipOutputStream zos) throws Exception {
        ZipEntry entry = new ZipEntry(entryName);
        zos.putNextEntry(entry);
        BufferedInputStream bis = new BufferedInputStream(is);
        int avail = bis.available();
        byte[] buffer = new byte[avail];
        bis.read(buffer, 0, avail);
        zos.write(buffer, 0, avail);
        zos.closeEntry();
    }
