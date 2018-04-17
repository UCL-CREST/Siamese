    public static String createMD5Hash(String passwd) throws GeneralSecurityException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(passwd.getBytes(), 0, passwd.length());
        return new BigInteger(1, messageDigest.digest()).toString(16);
    }
