    public long calc_crc32() {
        CRC32 cc = new CRC32();
        cc.reset();
        cc.update(bytes);
        return cc.getValue();
    }
