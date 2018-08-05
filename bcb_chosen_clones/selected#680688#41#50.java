    public static String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        if (text == null || text.length() < 1) {
            return null;
        }
        MessageDigest md = MessageDigest.getInstance(TYPE_SHA);
        md.update(text.getBytes(ENCODE), 0, text.length());
        byte[] sha1hash = new byte[40];
        sha1hash = md.digest();
        return convertToHexFormat(sha1hash);
    }
