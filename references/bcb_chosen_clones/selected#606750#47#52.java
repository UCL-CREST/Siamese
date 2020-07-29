    public boolean check_crc32_rawdata(int crc32) {
        CRC32 cc = new CRC32();
        cc.reset();
        cc.update(bytes);
        return cc.getValue() == crc32;
    }
