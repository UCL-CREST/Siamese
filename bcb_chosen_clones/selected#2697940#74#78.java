    public static final long crc32(byte[] data, int off, int len) {
        CRC32 crc32 = new CRC32();
        crc32.update(data, off, len);
        return crc32.getValue();
    }
