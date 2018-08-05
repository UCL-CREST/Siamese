    private void writeEntry(byte[] data, String name, ZipOutputStream outStream) throws IOException {
        ZipEntry entry = new ZipEntry(name);
        entry.setSize(data.length);
        entry.setTime(System.currentTimeMillis());
        if (outStream != null) {
            outStream.putNextEntry(entry);
            outStream.write(data);
            outStream.closeEntry();
        }
    }
