    private void addIndex(JarIndex index, ZipOutputStream zos) throws IOException {
        ZipEntry e = new ZipEntry(INDEX_NAME);
        e.setTime(System.currentTimeMillis());
        if (flag0) {
            CRC32OutputStream os = new CRC32OutputStream();
            index.write(os);
            os.updateEntry(e);
        }
        zos.putNextEntry(e);
        index.write(zos);
        zos.closeEntry();
    }
