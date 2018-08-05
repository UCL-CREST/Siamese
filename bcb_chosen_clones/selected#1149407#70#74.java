    public static final long crc32(final byte[] data, final int off, final int len) {
        final CRC32 crc32 = new CRC32();
        crc32.update(data, off, len);
        return crc32.getValue();
    }
