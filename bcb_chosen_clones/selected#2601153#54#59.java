    public static synchronized String encrypt(String plaintext) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(plaintext.getBytes("UTF-8"));
        byte raw[] = md.digest();
        return (new BASE64Encoder()).encode(raw);
    }
