    public static synchronized String hash(String plaintext) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        try {
            md.update(plaintext.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        byte raw[] = md.digest();
        return (new BASE64Encoder()).encode(raw);
    }
