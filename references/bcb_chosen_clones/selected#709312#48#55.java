    public static long calculateChecksum(InputStream stream) throws IOException {
        CRC32 crc32 = new CRC32();
        int read;
        while ((read = stream.read()) != -1) {
            crc32.update(read);
        }
        return crc32.getValue();
    }
