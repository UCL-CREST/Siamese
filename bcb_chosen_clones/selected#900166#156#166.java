    public static String md5hash(String text) {
        java.security.MessageDigest md;
        try {
            md = java.security.MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        md.update(text.getBytes());
        byte[] md5bytes = md.digest();
        return new String(org.apache.commons.codec.binary.Hex.encodeHex(md5bytes));
    }
