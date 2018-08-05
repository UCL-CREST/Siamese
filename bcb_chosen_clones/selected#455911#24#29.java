    public static final String CRC32(String value) {
        CRC32 crc32 = new CRC32();
        crc32.update(value.getBytes());
        BigInteger newValue = new BigInteger(crc32.getValue() + "");
        return newValue.toString(36);
    }
