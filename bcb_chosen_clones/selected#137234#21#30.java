    public static String hash(String string, String algorithm, String encoding) throws UnsupportedEncodingException {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            digest.update(string.getBytes(encoding));
            byte[] encodedPassword = digest.digest();
            return new BigInteger(1, encodedPassword).toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
