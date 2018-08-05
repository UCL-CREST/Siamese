    public static String generateHash(String msg) throws NoSuchAlgorithmException {
        if (msg == null) {
            throw new IllegalArgumentException("Input string can not be null");
        }
        MessageDigest m = MessageDigest.getInstance("MD5");
        m.reset();
        m.update(msg.getBytes());
        byte[] digest = m.digest();
        BigInteger bigInt = new BigInteger(1, digest);
        String hashText = bigInt.toString(16);
        while (hashText.length() < 32) {
            hashText = "0" + hashText;
        }
        return hashText;
    }
