    public static void copyJar(JarFile src, JarOutputStream dest) throws IOException {
        Enumeration entries = src.entries();
        while (entries.hasMoreElements()) {
            ZipEntry zse = (ZipEntry) entries.nextElement();
            if (zse.getName().equals("META-INF/MODULEINFO")) continue;
            if (zse.getName().equals("META-INF/")) continue;
            dest.putNextEntry(zse);
            InputStream fis = src.getInputStream(zse);
            byte[] b = new byte[2048];
            int n;
            while ((n = fis.read(b)) != -1) {
                dest.write(b, 0, n);
            }
        }
    }
