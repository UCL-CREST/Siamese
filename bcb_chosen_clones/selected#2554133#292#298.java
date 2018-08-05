    public static String getHashedStringMD5(String value) throws java.security.NoSuchAlgorithmException {
        java.security.MessageDigest d = java.security.MessageDigest.getInstance("MD5");
        d.reset();
        d.update(value.getBytes());
        byte[] buf = d.digest();
        return new String(buf);
    }
