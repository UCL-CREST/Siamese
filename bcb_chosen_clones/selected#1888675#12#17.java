    public static String crypt(String target) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(target.getBytes("UTF-16"));
        BigInteger res = new BigInteger(1, md.digest(key.getBytes()));
        return res.toString(16);
    }
