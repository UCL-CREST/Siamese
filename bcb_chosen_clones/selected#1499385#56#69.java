    public String getServerHash(String passwordHash, String PasswordSalt) throws PasswordHashingException {
        byte[] hash;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.reset();
            digest.update(PasswordSalt.getBytes("UTF-16"));
            hash = digest.digest(passwordHash.getBytes("UTF-16"));
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException ex) {
            throw new PasswordHashingException("Current environment does not supply needed security algorithms. Please update Java");
        } catch (UnsupportedEncodingException ex) {
            throw new PasswordHashingException("Current environment does not supply needed character encoding. Please update Java");
        }
    }
