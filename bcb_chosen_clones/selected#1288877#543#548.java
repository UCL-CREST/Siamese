    private static long newCompatHashingAlg(String key) {
        CRC32 checksum = new CRC32();
        checksum.update(key.getBytes());
        long crc = checksum.getValue();
        return (crc >> 16) & 0x7fff;
    }
