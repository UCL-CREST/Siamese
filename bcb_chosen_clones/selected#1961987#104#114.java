    public static byte[] generateHash(String strPassword, byte[] salt) {
        try {
            MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
            md.update(strPassword.getBytes(CHAR_ENCODING));
            md.update(salt);
            return md.digest();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
