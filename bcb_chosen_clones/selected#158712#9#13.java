    public static String getMd5(String message) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        digest.update(message.getBytes(), 0, message.length());
        return new BigInteger(1, digest.digest()).toString(16);
    }
