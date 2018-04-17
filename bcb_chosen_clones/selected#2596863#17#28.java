    public static byte[] encrypt(String string) {
        java.security.MessageDigest messageDigest = null;
        try {
            messageDigest = java.security.MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException exc) {
            logger.fatal(exc);
            throw new RuntimeException();
        }
        messageDigest.reset();
        messageDigest.update(string.getBytes());
        return messageDigest.digest();
    }
