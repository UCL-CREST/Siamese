    public static String getHash(String password) {
        if (password == null || password.length() == 0) {
            return null;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA");
            password = saltPassword(password);
            digest.update(password.getBytes());
            String result = getHexString(digest.digest());
            return result;
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }
