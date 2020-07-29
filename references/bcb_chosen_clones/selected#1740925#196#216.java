    public void test_write$BII_2() throws IOException {
        File f1 = File.createTempFile("testZip1", "tst");
        f1.deleteOnExit();
        FileOutputStream stream1 = new FileOutputStream(f1);
        ZipOutputStream zip1 = new ZipOutputStream(stream1);
        zip1.putNextEntry(new ZipEntry("one"));
        zip1.setMethod(ZipOutputStream.STORED);
        zip1.setMethod(ZipEntry.STORED);
        zip1.write(new byte[2]);
        try {
            zip1.putNextEntry(new ZipEntry("Second"));
            fail("ZipException expected");
        } catch (ZipException e) {
        }
        try {
            zip1.write(new byte[2]);
            fail("expected IOE there");
        } catch (IOException e2) {
        }
        zip1.close();
    }
