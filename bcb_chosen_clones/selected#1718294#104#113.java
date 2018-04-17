    public static String getHash(String password, String salt) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        logger.debug("Entering getHash with password = " + password + "\n and salt = " + salt);
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        digest.reset();
        digest.update(salt.getBytes());
        byte[] input = digest.digest(password.getBytes("UTF-8"));
        String hashResult = String.valueOf(input);
        logger.debug("Exiting getHash with hasResult of " + hashResult);
        return hashResult;
    }
