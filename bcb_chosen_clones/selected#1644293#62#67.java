     */
    public static long getCRCLongValue(byte[] b) {
        CRC32 crc = new CRC32();
        crc.reset();
        crc.update(b);
        return crc.getValue();
