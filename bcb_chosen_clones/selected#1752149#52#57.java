    public static long createChecksum(long random, String password) {
        if (password == null) return -1;
        CRC32 crc = new CRC32();
        crc.update(password.getBytes());
        return crc.getValue() * random;
    }
