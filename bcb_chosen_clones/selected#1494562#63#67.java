    public static String digest(String in, String algorithm) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        digest.update(in.getBytes());
        return bufferToHex(digest.digest());
    }
