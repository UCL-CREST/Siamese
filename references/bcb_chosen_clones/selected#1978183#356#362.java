    public static String generateEmailHash(String email) {
        email = email.trim().toLowerCase();
        CRC32 crc = new CRC32();
        crc.update(email.getBytes());
        String md5 = generateMD5(email);
        return crc.getValue() + "_" + md5;
    }
