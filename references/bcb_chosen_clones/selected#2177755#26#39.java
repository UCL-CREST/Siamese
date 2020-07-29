    public String encodePassword(String password, byte[] salt) throws Exception {
        if (salt == null) {
            salt = new byte[12];
            secureRandom.nextBytes(salt);
        }
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(salt);
        md.update(password.getBytes("UTF8"));
        byte[] digest = md.digest();
        byte[] storedPassword = new byte[digest.length + 12];
        System.arraycopy(salt, 0, storedPassword, 0, 12);
        System.arraycopy(digest, 0, storedPassword, 12, digest.length);
        return new String(Base64.encode(storedPassword));
    }
