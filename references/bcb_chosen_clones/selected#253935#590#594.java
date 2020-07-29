    private static byte[] SHA1(final String in) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(in.getBytes("iso-8859-1"), 0, in.length());
        return md.digest();
    }
