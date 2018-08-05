    public static synchronized String encrypt(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA");
        md.update(text.getBytes("UTF-8"));
        byte raw[] = md.digest();
        return new Base64(-1).encodeToString(raw);
    }
