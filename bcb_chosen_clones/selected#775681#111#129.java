    private static void buildJAR(Set classNames) throws IOException {
        Manifest manifest = new Manifest();
        manifest.getMainAttributes().putValue("Manifest-Version", "1.0");
        manifest.getMainAttributes().putValue("Main-Class", "jtcpfwd.Main");
        final JarOutputStream jos = new JarOutputStream(new FileOutputStream("jTCPfwd-lite-custom.jar"), manifest);
        final byte[] buf = new byte[4096];
        int len;
        for (Iterator it = classNames.iterator(); it.hasNext(); ) {
            String className = (String) it.next();
            final String classFileName = className.replace('.', '/') + ".class";
            jos.putNextEntry(new ZipEntry(classFileName));
            final InputStream in = CustomLiteBuilder.class.getResourceAsStream("/" + classFileName);
            while ((len = in.read(buf)) != -1) {
                jos.write(buf, 0, len);
            }
            in.close();
        }
        jos.close();
    }
