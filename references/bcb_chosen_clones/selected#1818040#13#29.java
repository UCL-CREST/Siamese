    public static String encrypt(String plaintext) throws NoSuchAlgorithmException {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            logger.error("unable to encrypt password" + e.getMessage());
            throw new NoSuchAlgorithmException(e.getMessage());
        }
        try {
            md.update(plaintext.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            logger.error("unable to encrypt password" + e.getMessage());
            throw new NoSuchAlgorithmException(e.getMessage());
        }
        byte raw[] = md.digest();
        return (new BASE64Encoder()).encode(raw);
    }
