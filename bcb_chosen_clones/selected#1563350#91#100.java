    private static String md5(String pwd) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(pwd.getBytes(), 0, pwd.length());
            return new BigInteger(1, md5.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new Error();
        }
    }
