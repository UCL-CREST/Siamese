    private int genChecksum() {
        CRC32 crc = new CRC32();
        crc.update(_prodCode);
        crc.update((int) _numUsers);
        crc.update(_expiry);
        return (int) crc.getValue();
    }
