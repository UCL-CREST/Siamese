    public synchronized String encrypt(String plaintext) throws PasswordException {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            throw new PasswordException(e.getMessage());
        }
        try {
            md.update(plaintext.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new PasswordException(e.getMessage());
        }
        byte raw[] = md.digest();
        String hash = (new Base64Encoder()).encode(raw);
        return hash;
    }
