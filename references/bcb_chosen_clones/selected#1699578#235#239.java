    public final long generateCheckSum(String value) {
        CRC32 crc = new CRC32();
        crc.update(value.getBytes());
        return crc.getValue();
    }
