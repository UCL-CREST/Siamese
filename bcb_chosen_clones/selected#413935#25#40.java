    public synchronized String encrypt(String plaintext) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA");
            md.update(plaintext.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            log().error("failed to encrypt the password.", e);
            throw new RuntimeException("failed to encrypt the password.", e);
        } catch (UnsupportedEncodingException e) {
            log().error("failed to encrypt the password.", e);
            throw new RuntimeException("failed to encrypt the password.", e);
        }
        byte raw[] = md.digest();
        String hash = (new BASE64Encoder()).encode(raw);
        return hash;
    }
