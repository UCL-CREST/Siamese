    private void writeEntry(JarOutputStream jos, ZipEntry entry, InputStream data) throws IOException {
        jos.putNextEntry(entry);
        byte[] newBytes = InputStreamUtil.createReadBuffer();
        int size = data.read(newBytes);
        while (size != -1) {
            jos.write(newBytes, 0, size);
            size = data.read(newBytes);
        }
        data.close();
    }
