    public void calcCRC() {
        CRC32 crc32 = new CRC32();
        byte[] typeBytes = new byte[4];
        typeBytes[0] = (byte) (type >> 24 & 0x00FF);
        typeBytes[1] = (byte) (type >> 16 & 0x00FF);
        typeBytes[2] = (byte) (type >> 8 & 0x00FF);
        typeBytes[3] = (byte) (type & 0x00FF);
        crc32.update(typeBytes);
        crc32.update(data);
        crc = (int) crc32.getValue();
    }
