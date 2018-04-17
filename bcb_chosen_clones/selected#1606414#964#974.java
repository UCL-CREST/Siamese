    private void addFileToZIP(File f, ZipEntry entry, ZipOutputStream zos) throws FileNotFoundException, IOException {
        FileInputStream in = new FileInputStream(f);
        zos.putNextEntry(entry);
        byte[] buf = new byte[256 * 1024];
        int len;
        while ((len = in.read(buf)) > -1) {
            zos.write(buf, 0, len);
        }
        zos.closeEntry();
        in.close();
    }
