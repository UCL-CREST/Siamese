    public static String unsecureHashConstantSalt(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        password = SALT3 + password;
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(password.getBytes(), 0, password.length());
        password += convertToHex(md5.digest()) + SALT4;
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        byte[] sha1hash = new byte[40];
        md.update(password.getBytes("UTF-8"), 0, password.length());
        sha1hash = md.digest();
        return convertToHex(sha1hash);
    }
