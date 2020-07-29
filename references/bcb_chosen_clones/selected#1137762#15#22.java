    public synchronized String encrypt(String plaintext) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = null;
        md = MessageDigest.getInstance("SHA");
        md.update(plaintext.getBytes("UTF-8"));
        byte raw[] = md.digest();
        String hash = (new BASE64Encoder()).encode(raw);
        return hash;
    }
