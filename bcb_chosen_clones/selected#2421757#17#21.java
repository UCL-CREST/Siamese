    public static String MD5(String val) throws NoSuchAlgorithmException {
        MessageDigest mdEnc = MessageDigest.getInstance("MD5");
        mdEnc.update(val.getBytes(), 0, val.length());
        return (new BigInteger(1, mdEnc.digest()).toString(16));
    }
