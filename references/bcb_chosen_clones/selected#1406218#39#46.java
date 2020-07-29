    public static String generateHashSE(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException, DigestException {
        MessageDigest md;
        md = MessageDigest.getInstance("SHA-256");
        byte[] hashSHA256 = new byte[32];
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        md.digest(hashSHA256, 0, text.length());
        return convertToHex(hashSHA256);
    }
