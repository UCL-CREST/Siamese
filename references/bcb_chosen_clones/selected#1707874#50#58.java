    private static String getBase64(String text, String algorithm) throws NoSuchAlgorithmException {
        AssertUtility.notNull(text);
        AssertUtility.notNullAndNotSpace(algorithm);
        String base64;
        MessageDigest md = MessageDigest.getInstance(algorithm);
        md.update(text.getBytes());
        base64 = new BASE64Encoder().encode(md.digest());
        return base64;
    }
