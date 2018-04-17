    public static final String encryptMD5(String decrypted) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(decrypted.getBytes());
            byte hash[] = md5.digest();
            md5.reset();
            return hashToHex(hash);
        } catch (NoSuchAlgorithmException _ex) {
            return null;
        }
    }
