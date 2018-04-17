    public static final String encryptSHA(String decrypted) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            sha.reset();
            sha.update(decrypted.getBytes());
            byte hash[] = sha.digest();
            sha.reset();
            return hashToHex(hash);
        } catch (NoSuchAlgorithmException _ex) {
            return null;
        }
    }
