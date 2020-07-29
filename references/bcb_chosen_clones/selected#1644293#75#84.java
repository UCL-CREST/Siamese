     */
    public static String getCRCStrValue(byte[] b) {
        CRC32 crc = new CRC32();
        crc.reset();
        crc.update(b);
        String s = Long.toHexString(crc.getValue());
        while (s.length() < 8) {
            s = "0" + s;
        }
        return s.toUpperCase();
