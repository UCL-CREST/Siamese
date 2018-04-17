    public static String hashPassword(String password) {
        String hashStr = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(password.getBytes(Charset.defaultCharset()));
            BigInteger hash = new BigInteger(1, md5.digest());
            hashStr = hash.toString(16);
        } catch (NoSuchAlgorithmException e) {
            return password;
        }
        StringBuilder buffer = new StringBuilder(hashStr);
        while (buffer.length() < 32) {
            buffer.insert(0, '0');
        }
        return buffer.toString();
    }
