    protected void addFile(String filename, InputStream stream) throws IOException {
        ZipEntry entry = new ZipEntry(filename);
        byte[] chunk = new byte[1024];
        int len;
        _zip.putNextEntry(entry);
        while ((len = stream.read(chunk)) > 0) {
            _zip.write(chunk, 0, len);
        }
        _zip.closeEntry();
        _zip.flush();
    }
