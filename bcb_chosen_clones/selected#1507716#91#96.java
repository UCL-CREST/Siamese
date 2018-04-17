    protected String md5sum(String toCompute) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(toCompute.getBytes());
        java.math.BigInteger hash = new java.math.BigInteger(1, md.digest());
        return hash.toString(16);
    }
