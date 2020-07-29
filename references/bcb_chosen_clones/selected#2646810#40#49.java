    public String hash(String plainTextPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance(digestAlgorithm);
            digest.update(plainTextPassword.getBytes(charset));
            byte[] rawHash = digest.digest();
            return new String(Hex.encodeHex(rawHash));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
