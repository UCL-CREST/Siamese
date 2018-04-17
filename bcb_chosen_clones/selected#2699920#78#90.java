    private String hashPassword(String plainTextPassword) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            digest.update(plainTextPassword.getBytes());
            BASE64Encoder enc = new BASE64Encoder();
            return enc.encode(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            log.error(e);
            throw new RuntimeException(e);
        }
    }
