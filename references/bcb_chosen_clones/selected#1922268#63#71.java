    public static String generateDigest(String message, String DigestAlgorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(DigestAlgorithm);
            md.update(message.getBytes(), 0, message.length());
            return new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException nsae) {
            return null;
        }
    }
