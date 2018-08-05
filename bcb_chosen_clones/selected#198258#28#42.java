    public static String email_get_public_hash(String email) {
        try {
            if (email != null) {
                email = email.trim().toLowerCase();
                CRC32 crc32 = new CRC32();
                crc32.reset();
                crc32.update(email.getBytes());
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                md5.reset();
                return crc32.getValue() + " " + new String(md5.digest(email.getBytes()));
            }
        } catch (Exception e) {
        }
        return "";
    }
