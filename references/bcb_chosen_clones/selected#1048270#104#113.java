    public void setPassword(String plaintext) throws java.security.NoSuchAlgorithmException {
        StringBuffer encrypted = new StringBuffer();
        java.security.MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
        digest.update(plaintext.getBytes());
        byte[] digestArray = digest.digest();
        for (int i = 0; i < digestArray.length; i++) {
            encrypted.append(byte2hex(digestArray[i]));
        }
        setEncryptedPassword(encrypted.toString());
    }
