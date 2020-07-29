    public static final String encryptPassword(String loginName, String password) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(loginName.toUpperCase().getBytes("UTF-8"));
            md5.update(password.getBytes("UTF-8"));
            byte[] ba = md5.digest();
            return byte2hex(ba);
        } catch (Exception e) {
            return password;
        }
    }
