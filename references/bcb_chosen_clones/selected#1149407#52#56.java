    public static final long crc32(final byte[] data) {
        final CRC32 crc32 = new CRC32();
        crc32.update(data);
        return crc32.getValue();
    }
