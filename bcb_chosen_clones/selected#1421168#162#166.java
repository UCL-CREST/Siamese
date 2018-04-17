    public static String SHA1(final String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        final MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(text.getBytes("utf-8"), 0, text.length());
        return convertToHex(md.digest());
    }
