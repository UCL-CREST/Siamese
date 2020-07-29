    public static String md5(String s) throws NoSuchAlgorithmException {
        MessageDigest digester = MessageDigest.getInstance("MD5");
        digester.update(s.getBytes());
        return new BigInteger(1, digester.digest()).toString(16);
    }
