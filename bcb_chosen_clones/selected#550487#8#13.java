    public static String encrypt(String x) throws Exception {
        MessageDigest mdEnc = MessageDigest.getInstance("SHA-1");
        mdEnc.update(x.getBytes(), 0, x.length());
        String md5 = new BigInteger(1, mdEnc.digest()).toString(16);
        return md5;
    }
