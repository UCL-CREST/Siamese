    private static long crc32(byte[] stream) {
        CRC32 crc32 = new CRC32();
        crc32.reset();
        crc32.update(stream);
        long res = crc32.getValue();
        return res;
    }
