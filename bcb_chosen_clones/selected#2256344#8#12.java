    public String hash(String var) throws Exception {
        MessageDigest m = MessageDigest.getInstance("MD5");
        m.update(var.getBytes(), 0, var.length());
        return new BigInteger(1, m.digest()).toString(16);
    }
