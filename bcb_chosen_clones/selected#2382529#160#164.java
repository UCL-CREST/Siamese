    public static long textToInt(String txt) {
        CRC32 crc = new CRC32();
        crc.update(txt.getBytes());
        return crc.getValue();
    }
