    public static synchronized String encrypt(String plaintextPassword) throws Exception {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            throw new Exception(e);
        }
        try {
            md.update(plaintextPassword.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new Exception(e);
        }
        byte raw[] = md.digest();
        String hash = (new BASE64Encoder()).encode(raw);
        return hash;
    }
