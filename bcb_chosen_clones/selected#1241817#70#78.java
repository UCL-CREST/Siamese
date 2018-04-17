    public static long getCrc(InputStream in) throws IOException {
        CRC32 crc = new CRC32();
        byte[] buff = new byte[65536];
        int count = 0;
        while ((count = in.read(buff)) >= 0) {
            crc.update(buff, 0, count);
        }
        return (crc.getValue());
    }
