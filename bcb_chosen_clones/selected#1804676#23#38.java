    public void setUp() throws IOException {
        tmp = TestingUtil.tmpFile(new File("."));
        ZipOutputStream os = new ZipOutputStream(new FileOutputStream(tmp));
        os.putNextEntry(new ZipEntry("key0"));
        os.write(0x01);
        os.write(0x05);
        os.closeEntry();
        os.putNextEntry(new ZipEntry("key1"));
        os.write(0x02);
        os.closeEntry();
        os.putNextEntry(new ZipEntry("properties"));
        os.write("foo=bar\n".getBytes());
        os.closeEntry();
        os.close();
        storer = new ZipStorer(new ZipFile(tmp));
    }
