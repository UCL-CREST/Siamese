    private String hash(String text) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(text.getBytes());
        BigInteger hash = new BigInteger(1, md5.digest());
        return hash.toString(16);
    }
