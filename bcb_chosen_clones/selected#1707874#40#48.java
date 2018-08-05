    private static String getHashString(String text, String algorithm) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        AssertUtility.notNull(text);
        AssertUtility.notNullAndNotSpace(algorithm);
        MessageDigest md;
        md = MessageDigest.getInstance(algorithm);
        md.update(text.getBytes("UTF-8"), 0, text.length());
        byte[] hash = md.digest();
        return convertToHex(hash);
    }
