    public static BigInteger getSHA(String str) throws NoSuchAlgorithmException {
        MessageDigest sha = MessageDigest.getInstance("SHA");
        sha.update(str.getBytes());
        return new BigInteger(sha.digest());
    }
