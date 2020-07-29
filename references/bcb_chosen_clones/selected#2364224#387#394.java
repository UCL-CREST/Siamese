    public static String SHA512(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md;
        md = MessageDigest.getInstance("SHA-512");
        byte[] sha1hash = new byte[40];
        md.update(text.getBytes("UTF-8"), 0, text.length());
        sha1hash = md.digest();
        return convertToHex(sha1hash);
    }
