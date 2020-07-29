    public static String cryptSha(String target) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA");
        md.update(target.getBytes("UTF-16"));
        BigInteger res = new BigInteger(1, md.digest(key.getBytes()));
        return res.toString(16);
    }
