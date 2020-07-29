    public static final int getCRC32(byte[] data, int length) {
        CRC32 crc = new CRC32();
        crc.update(data);
        return (int) crc.getValue();
    }
