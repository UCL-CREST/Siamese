    public void test_setLevelI() throws IOException {
        ZipEntry ze = new ZipEntry("test");
        zos.putNextEntry(ze);
        zos.write(data.getBytes());
        zos.closeEntry();
        long csize = ze.getCompressedSize();
        zos.setLevel(9);
        zos.putNextEntry(ze = new ZipEntry("test2"));
        zos.write(data.getBytes());
        zos.closeEntry();
        assertTrue("setLevel failed", csize <= ze.getCompressedSize());
    }
