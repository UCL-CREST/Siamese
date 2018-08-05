    public String hash(String plainTextPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance(digestAlgorithm);
            if (saltPhrase != null) {
                digest.update(saltPhrase.getBytes(charset));
                byte[] salt = digest.digest();
                digest.reset();
                digest.update(plainTextPassword.getBytes(charset));
                digest.update(salt);
            } else {
                digest.update(plainTextPassword.getBytes(charset));
            }
            byte[] rawHash = digest.digest();
            if (encoding != null && encoding.equals(Encoding.base64)) {
                return Base64.encodeBytes(rawHash);
            } else {
                return new String(Hex.encodeHex(rawHash));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
