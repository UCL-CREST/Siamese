    public PassiveCheckBytesBuilder writeCRC() {
        final CRC32 crc = new CRC32();
        crc.update(bytes);
        ByteArrayUtils.writeInteger(bytes, (int) crc.getValue(), 4);
        return this;
    }
