    private static String encode(final String input) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.reset();
        md.update(input.getBytes("UTF-8"));
        return toHexString(md.digest());
    }
