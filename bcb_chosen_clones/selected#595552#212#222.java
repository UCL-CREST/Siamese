    public static String MD5ToString(String md5) {
        String hashword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(md5.getBytes());
            BigInteger hash = new BigInteger(1, md.digest());
            hashword = hash.toString(16);
        } catch (NoSuchAlgorithmException nsae) {
        }
        return hashword;
    }
