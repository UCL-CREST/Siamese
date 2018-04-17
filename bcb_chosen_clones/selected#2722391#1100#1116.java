    private void addToJar(JarOutputStream out, InputStream in, String entryName, long length) throws IOException {
        byte[] buf = new byte[2048];
        ZipEntry entry = new ZipEntry(entryName);
        CRC32 crc = new CRC32();
        entry.setSize(length);
        entry.setCrc(crc.getValue());
        out.putNextEntry(entry);
        int read = in.read(buf);
        while (read > 0) {
            crc.update(buf, 0, read);
            out.write(buf, 0, read);
            read = in.read(buf);
        }
        entry.setCrc(crc.getValue());
        in.close();
        out.closeEntry();
    }
