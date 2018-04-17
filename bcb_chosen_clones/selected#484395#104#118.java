    private static void add(JarEntry entry, InputStream in, JarOutputStream out, CRC32 crc, byte[] buffer) throws IOException {
        out.putNextEntry(entry);
        int read;
        long size = 0;
        while ((read = in.read(buffer)) != -1) {
            crc.update(buffer, 0, read);
            out.write(buffer, 0, read);
            size += read;
        }
        entry.setCrc(crc.getValue());
        entry.setSize(size);
        in.close();
        out.closeEntry();
        crc.reset();
    }
