    public String setCrc32() {
        CRC32 crc = new CRC32();
        crc.update(file);
        crc32 = "" + crc.getValue();
        return crc32;
    }
