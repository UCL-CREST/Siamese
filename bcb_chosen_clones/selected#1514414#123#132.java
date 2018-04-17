    public static long createCRC32Checksum(InputStream in) throws IOException {
        byte[] buf = new byte[BUFFER_SIZE];
        int length = -1;
        CRC32 crc = new CRC32();
        while ((length = in.read(buf)) > 0) {
            crc.update(buf, 0, length);
        }
        long checksum = crc.getValue();
        return checksum;
    }
