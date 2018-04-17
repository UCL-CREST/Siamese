    public synchronized String getEncryptedPassword(String plaintext, String algorithm) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = null;
        md = MessageDigest.getInstance(algorithm);
        md.update(plaintext.getBytes("UTF-8"));
        return bytesToHexString(md.digest());
    }
