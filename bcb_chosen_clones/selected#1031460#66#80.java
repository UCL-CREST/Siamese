    public String encodePassword(String rawPass, Object salt) {
        MessageDigest sha;
        try {
            sha = MessageDigest.getInstance("SHA");
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new LdapDataAccessException("No SHA implementation available!");
        }
        sha.update(rawPass.getBytes());
        if (salt != null) {
            Assert.isInstanceOf(byte[].class, salt, "Salt value must be a byte array");
            sha.update((byte[]) salt);
        }
        byte[] hash = combineHashAndSalt(sha.digest(), (byte[]) salt);
        return (salt == null ? SHA_PREFIX : SSHA_PREFIX) + new String(Base64.encodeBase64(hash));
    }
