    public void putNextEntry(ZipEntry entry, byte[] data) throws IOException {
        out.putNextEntry(entry);
        out.write(data, 0, data.length);
        out.flush();
    }
