    public static synchronized String toMD5(String str) {
        Nulls.failIfNull(str, "Cannot create an MD5 encryption form a NULL string");
        String hashword = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance(MD5);
            md5.update(str.getBytes());
            BigInteger hash = new BigInteger(1, md5.digest());
            hashword = hash.toString(16);
            return Strings.padLeft(hashword, 32, "0");
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return hashword;
    }
