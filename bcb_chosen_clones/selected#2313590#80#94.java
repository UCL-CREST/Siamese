    public static String getEncryptedPwd(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        byte[] pwd = null;
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        MessageDigest md = null;
        md = MessageDigest.getInstance("MD5");
        md.update(salt);
        md.update(password.getBytes("UTF-8"));
        byte[] digest = md.digest();
        pwd = new byte[digest.length + SALT_LENGTH];
        System.arraycopy(salt, 0, pwd, 0, SALT_LENGTH);
        System.arraycopy(digest, 0, pwd, SALT_LENGTH, digest.length);
        return byteToHexString(pwd);
    }
