    private static long _getCRC(int type, byte[] data) {
        CRC32 crc = new CRC32();
        crc.update((type >> 24) & 0x000000ff);
        crc.update((type >> 16) & 0x000000ff);
        crc.update((type >> 8) & 0x000000ff);
        crc.update(type & 0x000000ff);
        if (data != null) crc.update(data);
        return crc.getValue();
    }
