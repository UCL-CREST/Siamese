    public void test_setMethodI() throws IOException {
        ZipEntry ze = new ZipEntry("test");
        zos.setMethod(ZipOutputStream.STORED);
        CRC32 tempCrc = new CRC32();
        tempCrc.update(data.getBytes());
        ze.setCrc(tempCrc.getValue());
        ze.setSize(new String(data).length());
        zos.putNextEntry(ze);
        zos.write(data.getBytes());
        zos.closeEntry();
        long csize = ze.getCompressedSize();
        zos.setMethod(ZipOutputStream.DEFLATED);
        zos.putNextEntry(ze = new ZipEntry("test2"));
        zos.write(data.getBytes());
        zos.closeEntry();
        assertTrue("setLevel failed", csize >= ze.getCompressedSize());
    }
