    private void writeEntry(JarOutputStream jos, JarEntry entry, InputStream data) throws IOException {
        jos.putNextEntry(new ZipEntry(entry.getName()));
        int size = data.read(newBytes);
        while (size != -1) {
            jos.write(newBytes, 0, size);
            size = data.read(newBytes);
        }
        data.close();
    }
