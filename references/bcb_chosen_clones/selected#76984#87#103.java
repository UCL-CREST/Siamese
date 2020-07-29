    public static void addToJar(JarOutputStream jos, JarFile jf) throws IOException {
        Enumeration e = jf.entries();
        while (e.hasMoreElements()) {
            ZipEntry je = (ZipEntry) e.nextElement();
            InputStream io = jf.getInputStream(je);
            byte b[] = new byte[4096];
            int read = 0;
            try {
                jos.putNextEntry(je);
                while ((read = io.read(b, 0, 4096)) != -1) {
                    jos.write(b, 0, read);
                }
            } catch (ZipException ze) {
                throw ze;
            }
        }
    }
