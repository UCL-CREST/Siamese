    public static String getPasswordHash(String password) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
        md.update(password.getBytes());
        byte[] digest = md.digest();
        BigInteger i = new BigInteger(1, digest);
        String hash = i.toString(16);
        while (hash.length() < 32) {
            hash = "0" + hash;
        }
        return hash;
    }
