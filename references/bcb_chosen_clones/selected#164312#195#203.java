    private String crcHeader(byte[] bytes) {
        CRC32 crc = new CRC32();
        crc.update(bytes);
        int realCRC = (int) crc.getValue();
        realCRC = realCRC ^ (-1);
        realCRC = realCRC >>> 0;
        String hexCRC = Integer.toHexString(realCRC).substring(0, 4);
        return hexCRC.toString().toUpperCase();
    }
