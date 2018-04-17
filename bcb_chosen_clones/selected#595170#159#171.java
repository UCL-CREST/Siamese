    public void copyToZip(ZipOutputStream zout, String entryName) throws IOException {
        close();
        ZipEntry entry = new ZipEntry(entryName);
        zout.putNextEntry(entry);
        if (!isEmpty() && this.tmpFile.exists()) {
            InputStream in = new FileInputStream(this.tmpFile);
            IOUtils.copyTo(in, zout);
            in.close();
        }
        zout.flush();
        zout.closeEntry();
        delete();
    }
