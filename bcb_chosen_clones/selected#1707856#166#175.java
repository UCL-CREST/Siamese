    void copyFileToZip(String entryName, String filename, ZipOutputStream zos) throws Exception {
        ZipEntry entry = new ZipEntry(entryName);
        zos.putNextEntry(entry);
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filename));
        int avail = bis.available();
        byte[] buffer = new byte[avail];
        bis.read(buffer, 0, avail);
        zos.write(buffer, 0, avail);
        zos.closeEntry();
    }
