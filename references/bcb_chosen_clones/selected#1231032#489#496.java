    private static int setHeaderChecksum(byte[] header) {
        writeInt(header, I_CHECKSUM, 0);
        CRC32 crc = new CRC32();
        crc.update(header, 0, MINIMUM_PAGE_SIZE);
        int checksum = (int) crc.getValue();
        writeInt(header, I_CHECKSUM, checksum);
        return checksum;
    }
