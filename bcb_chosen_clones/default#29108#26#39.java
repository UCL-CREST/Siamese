    File createJar() throws IOException {
        byte[] dummy_data = new byte[10];
        File f = new File("a b.jar");
        OutputStream out = new FileOutputStream(f);
        try {
            JarOutputStream jar = new JarOutputStream(out);
            jar.putNextEntry(new ZipEntry("dummy.class"));
            jar.write(dummy_data);
            jar.close();
        } finally {
            out.close();
        }
        return f;
    }
