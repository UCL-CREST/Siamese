    private static String encrypt(String s, String alg, String enc) throws Exception {
        MessageDigest md = (MessageDigest) MessageDigest.getInstance(alg).clone();
        md.update(s.getBytes(enc));
        return (new BASE64Encoder()).encode(md.digest());
    }
