    public void addReader(String archiveName, Reader reader) throws IOException {
        ZipEntry entry = new ZipEntry(archiveName);
        entry.setMethod(ZipEntry.DEFLATED);
        putNextEntry(entry);
        OutputStreamWriter writer = new OutputStreamWriter(this);
        char[] buffer = new char[1024];
        int readChars = 0;
        while ((readChars = reader.read(buffer, 0, buffer.length)) > 0) {
            writer.write(buffer, 0, readChars);
        }
        writer.flush();
        closeEntry();
    }
