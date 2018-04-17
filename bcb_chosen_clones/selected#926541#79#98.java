    public void test_JarOutputStreamLjava_io_OutputStream() throws IOException {
        File fooJar = File.createTempFile("hyts_", ".jar");
        FileOutputStream fos = new FileOutputStream(fooJar);
        ZipEntry ze = new ZipEntry("Test");
        try {
            JarOutputStream joutFoo = new JarOutputStream(fos);
            joutFoo.putNextEntry(ze);
            joutFoo.write(33);
        } catch (IOException ee) {
            fail("IOException is not expected");
        }
        fos.close();
        fooJar.delete();
        try {
            JarOutputStream joutFoo = new JarOutputStream(fos);
            joutFoo.putNextEntry(ze);
            fail("IOException expected");
        } catch (IOException ee) {
        }
    }
