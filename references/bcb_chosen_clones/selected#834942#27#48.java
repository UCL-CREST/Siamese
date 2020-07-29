    public String hash(String password) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException ex) {
            log.info("No sha-256 available");
            try {
                digest = MessageDigest.getInstance("SHA-1");
            } catch (NoSuchAlgorithmException e) {
                log.fatal("sha-1 is not available", e);
                throw new RuntimeException("Couldn't get a hash algorithm from Java");
            }
        }
        try {
            digest.reset();
            digest.update((salt + password).getBytes("UTF-8"));
            byte hash[] = digest.digest();
            return new String(Base64.encodeBase64(hash, false));
        } catch (Throwable t) {
            throw new RuntimeException("Couldn't hash password");
        }
    }
