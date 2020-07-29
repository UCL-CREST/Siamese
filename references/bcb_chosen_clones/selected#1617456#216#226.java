    public static String encryptMD5(String str) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(str.getBytes());
            byte[] hash = md5.digest();
            md5.reset();
            return Format.hashToHex(hash);
        } catch (java.security.NoSuchAlgorithmException nsae0) {
            return null;
        }
    }
