    public void putEntry(String entryName, File file) throws ZipException, IOException {
        byte[] buf = new byte[1024];
        int len;
        if (zos == null) throw new ZipException("IMSCP.putEntry(File file): ZipOutputstream not initialized");
        try {
            ZipEntry ze = new ZipEntry(entryName);
            zos.putNextEntry(ze);
            BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file));
            while ((len = fis.read(buf)) > 0) {
                zos.write(buf, 0, len);
            }
            fis.close();
            zos.closeEntry();
        } catch (ZipException ex) {
            if (!ex.getMessage().startsWith("duplicate")) throw ex;
        }
    }
