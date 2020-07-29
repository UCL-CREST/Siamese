    public String getCRC32(File inDir) {
        String temp = createHash(inDir);
        CRC32 crc32 = new CRC32();
        crc32.update(temp.getBytes());
        return getHexValue(crc32.getValue());
    }
