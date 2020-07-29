    private int genChecksum() {
        CRC32 crc = new CRC32();
        crc.update(_prodCode);
        crc.update(_random);
        return (int) crc.getValue();
    }
