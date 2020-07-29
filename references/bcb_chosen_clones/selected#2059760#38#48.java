    public static String encodeMD5(String s) throws NoSuchAlgorithmException {
        MessageDigest m = null;
        String result = null;
        m = MessageDigest.getInstance("MD5");
        m.update(s.getBytes(), 0, s.length());
        result = new BigInteger(1, m.digest()).toString(16);
        if (result.length() == 31) {
            result = "0" + result;
        }
        return result;
    }
