    private static int newCompatHashingAlg(String key) {
        CRC32 checksum = new CRC32();
        checksum.update(key.getBytes());
        int crc = (int) checksum.getValue();
        return (crc >> 16) & 0x7fff;
    }
