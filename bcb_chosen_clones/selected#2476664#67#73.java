    public String encrypt(String password) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(password.getBytes());
        BigInteger hash = new BigInteger(1, md5.digest());
        String hashword = hash.toString(16);
        return hashword;
    }
