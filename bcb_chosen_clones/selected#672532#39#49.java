    public void addStream(String archiveName, InputStream input) throws IOException {
        ZipEntry entry = new ZipEntry(archiveName);
        entry.setMethod(ZipEntry.DEFLATED);
        putNextEntry(entry);
        byte[] buffer = new byte[1024];
        int readBytes = 0;
        while ((readBytes = input.read(buffer, 0, buffer.length)) > 0) {
            write(buffer, 0, readBytes);
        }
        closeEntry();
    }
