    protected static byte[] hashPassword(byte[] saltBytes, String plaintextPassword) throws AssertionError {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            throw (AssertionError) new AssertionError("No MD5 message digest supported.").initCause(ex);
        }
        digest.update(saltBytes);
        try {
            digest.update(plaintextPassword.getBytes("utf-8"));
        } catch (UnsupportedEncodingException ex) {
            throw (AssertionError) new AssertionError("No UTF-8 encoding supported.").initCause(ex);
        }
        byte[] passwordBytes = digest.digest();
        return passwordBytes;
    }
