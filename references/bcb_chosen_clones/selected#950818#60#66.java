    private static String getUnsaltedHash(String algorithm, String input) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
        messageDigest.reset();
        messageDigest.update(input.getBytes(Main.DEFAULT_CHARSET));
        byte[] digest = messageDigest.digest();
        return String.format(Main.DEFAULT_LOCALE, "%0" + (digest.length << 1) + "x", new BigInteger(1, digest));
    }
