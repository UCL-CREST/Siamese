    protected void setUp() throws Exception {
        super.setUp();
        jar = File.createTempFile("test", ".jar");
        jar.deleteOnExit();
        JarOutputStream jout = new JarOutputStream(new FileOutputStream(jar));
        ZipEntry zip = new ZipEntry("oqube/patchwork/Dummy.class");
        InputStream is = getClass().getResourceAsStream("/main.bin");
        jout.putNextEntry(zip);
        byte[] buf = new byte[1024];
        int rd = 0;
        while ((rd = is.read(buf, 0, 1024)) != -1) {
            jout.write(buf, 0, rd);
        }
        jout.closeEntry();
        jout.finish();
        jout.flush();
        jout.close();
        dir = jar.getParentFile();
    }
