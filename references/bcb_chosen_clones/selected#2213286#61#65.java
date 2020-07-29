    public static long crc32(String s) {
        java.util.zip.CRC32 crc32 = new java.util.zip.CRC32();
        crc32.update(s.getBytes());
        return crc32.getValue();
    }
