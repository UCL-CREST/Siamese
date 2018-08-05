    public static String getEncryptedPassword(String password) throws PasswordException {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA");
            md.update(password.getBytes("UTF-8"));
        } catch (Exception e) {
            throw new PasswordException(e);
        }
        return convertToString(md.digest());
    }
