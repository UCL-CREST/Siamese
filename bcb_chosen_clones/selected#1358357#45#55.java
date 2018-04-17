    public static String getMD5(String password) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            String salt = "UseTheForce4";
            password = salt + password;
            md5.update(password.getBytes(), 0, password.length());
            password = new BigInteger(1, md5.digest()).toString(16);
        } catch (Exception e) {
        }
        return password;
    }
