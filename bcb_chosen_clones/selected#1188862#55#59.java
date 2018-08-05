    public static boolean verifyMessageDigest(String message, String algorithm, byte[] digest) throws NoSuchAlgorithmException {
        MessageDigest alg = MessageDigest.getInstance(algorithm);
        alg.update(message.getBytes());
        return MessageDigest.isEqual(digest, alg.digest());
    }
