    public static String toSHA(String message) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        digest.update(message.getBytes());
        return DHKeyExchange.toHexString(digest.digest());
    }
