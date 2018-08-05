    public void test_write$BII() throws IOException {
        ZipEntry ze = new ZipEntry("test");
        zos.putNextEntry(ze);
        zos.write(data.getBytes());
        zos.closeEntry();
        zos.close();
        zos = null;
        zis = new ZipInputStream(new ByteArrayInputStream(bos.toByteArray()));
        zis.getNextEntry();
        byte[] b = new byte[data.length()];
        int r = 0;
        int count = 0;
        while (count != b.length && (r = zis.read(b, count, b.length)) != -1) {
            count += r;
        }
        zis.closeEntry();
        assertTrue("Write failed to write correct bytes", new String(b).equals(data));
        File f = File.createTempFile("testZip", "tst");
        f.deleteOnExit();
        FileOutputStream stream = new FileOutputStream(f);
        ZipOutputStream zip = new ZipOutputStream(stream);
        zip.setMethod(ZipEntry.STORED);
        try {
            zip.putNextEntry(new ZipEntry("Second"));
            fail("Not set an entry. Should have thrown ZipException.");
        } catch (ZipException e) {
        }
        try {
            zip.write(new byte[2]);
            fail("Writing data without an entry. Should have thrown IOException");
        } catch (IOException e) {
        }
        try {
            zip.write(new byte[2], 0, 12);
            fail("Writing data without an entry. Should have thrown IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
        }
        try {
            zip.write(null, 0, -2);
            fail("Should throw IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
        }
        stream.close();
    }
