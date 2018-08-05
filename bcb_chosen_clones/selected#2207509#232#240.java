    private static void writeEntry(JarOutputStream jos, ZipEntry entry, InputStream data) throws IOException {
        jos.putNextEntry(entry);
        int size = data.read(newBytes);
        while (size != -1) {
            jos.write(newBytes, 0, size);
            size = data.read(newBytes);
        }
        data.close();
    }
