    public static boolean validPassword(String password, String passwordInDb) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        byte[] pwdInDb = hexStringToByte(passwordInDb);
        byte[] salt = new byte[SALT_LENGTH];
        System.arraycopy(pwdInDb, 0, salt, 0, SALT_LENGTH);
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(salt);
        md.update(password.getBytes("UTF-8"));
        byte[] digest = md.digest();
        byte[] digestInDb = new byte[pwdInDb.length - SALT_LENGTH];
        System.arraycopy(pwdInDb, SALT_LENGTH, digestInDb, 0, digestInDb.length);
        if (Arrays.equals(digest, digestInDb)) {
            return true;
        } else {
            return false;
        }
    }
