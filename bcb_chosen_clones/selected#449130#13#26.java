    @Override
    public String encodePassword(final String password, final Object salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.reset();
            digest.update(salt.toString().getBytes());
            byte[] passwordHash = digest.digest(password.getBytes());
            Base64 encoder = new Base64();
            byte[] encoded = encoder.encode(passwordHash);
            return new String(encoded);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
