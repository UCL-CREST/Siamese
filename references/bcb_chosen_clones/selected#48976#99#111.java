    public static String md5encrypt(String toEncrypt) {
        if (toEncrypt == null) {
            throw new IllegalArgumentException("null is not a valid password to encrypt");
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(toEncrypt.getBytes());
            byte[] hash = md.digest();
            return new String(dumpBytes(hash));
        } catch (NoSuchAlgorithmException nsae) {
            return toEncrypt;
        }
    }
