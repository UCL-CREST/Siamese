    static void copyJarFile(JarInputStream in, JarOutputStream out) throws IOException {
        if (in.getManifest() != null) {
            ZipEntry me = new ZipEntry(JarFile.MANIFEST_NAME);
            out.putNextEntry(me);
            in.getManifest().write(out);
            out.closeEntry();
        }
        byte[] buffer = new byte[1 << 14];
        for (JarEntry je; (je = in.getNextJarEntry()) != null; ) {
            out.putNextEntry(je);
            for (int nr; 0 < (nr = in.read(buffer)); ) {
                out.write(buffer, 0, nr);
            }
        }
        in.close();
        markJarFile(out);
    }
