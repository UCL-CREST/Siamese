    private static String calcReturnKey(String key) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md;
        md = MessageDigest.getInstance("SHA-1");
        String text = new String();
        byte[] sha1hash = new byte[20];
        text = key + GUUI;
        md.update(text.getBytes(), 0, text.length());
        sha1hash = md.digest();
        return (Helper.getBASE64(sha1hash));
    }
