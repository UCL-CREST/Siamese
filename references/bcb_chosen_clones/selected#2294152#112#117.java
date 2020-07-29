    private int crc32Hash(byte[] key) {
        CRC32 checksum = new CRC32();
        checksum.update(key);
        long crc = checksum.getValue();
        return (int) crc;
    }
