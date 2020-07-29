    public static byte[] crc32(String s) throws Exception {
        CRC32 crc32 = new CRC32();
        crc32.update(s.getBytes("utf-8"));
        long value = crc32.getValue();
        byte[] bytes = new byte[4];
        int i = 3;
        byte b;
        while (value != 0) {
            b = (byte) ((value % 256) - 128);
            value /= 256;
            bytes[i] = b;
            i--;
        }
        return bytes;
    }
