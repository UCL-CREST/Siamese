    public static String md5(String message) throws Exception {
        final MessageDigest m = MessageDigest.getInstance("MD5");
        m.update(message.getBytes(), 0, message.length());
        return new BigInteger(1, m.digest()).toString(16);
    }
