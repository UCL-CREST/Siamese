    public String hash(String plaintext, String salt, int iterations) throws EncryptionException {
        byte[] bytes = null;
        try {
            MessageDigest digest = MessageDigest.getInstance(hashAlgorithm);
            digest.reset();
            digest.update(ESAPI.securityConfiguration().getMasterSalt());
            digest.update(salt.getBytes(encoding));
            digest.update(plaintext.getBytes(encoding));
            bytes = digest.digest();
            for (int i = 0; i < iterations; i++) {
                digest.reset();
                bytes = digest.digest(bytes);
            }
            String encoded = ESAPI.encoder().encodeForBase64(bytes, false);
            return encoded;
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptionException("Internal error", "Can't find hash algorithm " + hashAlgorithm, e);
        } catch (UnsupportedEncodingException ex) {
            throw new EncryptionException("Internal error", "Can't find encoding for " + encoding, ex);
        }
    }
