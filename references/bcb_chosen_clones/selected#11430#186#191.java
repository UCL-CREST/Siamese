    public static int getCrc32asInt(byte[] in) {
        if (in == null || in.length == 0) return -1;
        CRC32 crc = new CRC32();
        crc.update(in);
        return (int) crc.getValue();
    }
