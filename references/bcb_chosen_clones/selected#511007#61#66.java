    public byte[] getHash(String plaintext) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.reset();
        digest.update(salt.getBytes("UTF-8"));
        return digest.digest(plaintext.getBytes("UTF-8"));
    }
