    public long crc(byte[] msg) {
        final CRC32 crc = new CRC32();
        crc.update(msg);
        return crc.getValue();
    }
