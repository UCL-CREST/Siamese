    protected static boolean verifyCRC(byte[] typeBytes, byte[] data, long crc) {
        CRC32 crc32 = new CRC32();
        crc32.update(typeBytes);
        crc32.update(data);
        long calculated = crc32.getValue();
        return (calculated == crc);
    }
