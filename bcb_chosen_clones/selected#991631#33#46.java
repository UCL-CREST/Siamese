    public String hash(String plainTextPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance(digestAlgorithm);
            digest.update(plainTextPassword.getBytes(charset));
            byte[] rawHash = digest.digest();
            return new String(org.jboss.seam.util.Hex.encodeHex(rawHash));
        } catch (NoSuchAlgorithmException e) {
            log.error("Digest algorithm #0 to calculate the password hash will not be supported.", digestAlgorithm);
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            log.error("The Character Encoding #0 is not supported", charset);
            throw new RuntimeException(e);
        }
    }
