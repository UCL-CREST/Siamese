    public static String encrypt(String plaintext) throws Exception {
        String algorithm = XML.get("security.algorithm");
        if (algorithm == null) algorithm = "SHA-1";
        MessageDigest md = MessageDigest.getInstance(algorithm);
        md.update(plaintext.getBytes("UTF-8"));
        return new BASE64Encoder().encode(md.digest());
    }
