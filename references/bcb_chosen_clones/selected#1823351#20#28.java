    public static String hash(String plainText) throws Exception {
        MessageDigest m = MessageDigest.getInstance("MD5");
        m.update(plainText.getBytes(), 0, plainText.length());
        String hash = new BigInteger(1, m.digest()).toString(16);
        if (hash.length() == 31) {
            hash = "0" + hash;
        }
        return hash;
    }
