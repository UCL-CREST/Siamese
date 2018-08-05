    public static String getMD5(String s) {
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(s.getBytes(), 0, s.length());
            return "" + new BigInteger(1, m.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            logger.error("MD5 is not supported !!!");
        }
        return s;
    }
