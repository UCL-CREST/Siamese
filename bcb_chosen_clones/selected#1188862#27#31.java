    public static byte[] getMessageDigest(String message, String algorithm) throws NoSuchAlgorithmException {
        MessageDigest alg = MessageDigest.getInstance(algorithm);
        alg.update(message.getBytes());
        return alg.digest();
    }
