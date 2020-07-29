    private byte[] digestPassword(byte[] salt, String password) throws AuthenticationException {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(salt);
            md.update(password.getBytes("UTF8"));
            return md.digest();
        } catch (Exception e) {
            throw new AuthenticationException(MESSAGE_CONFIGURATION_ERROR_KEY, e);
        }
    }
