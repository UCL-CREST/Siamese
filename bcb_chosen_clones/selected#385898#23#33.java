    public static String encrypt(String text) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            throw new WebDocRuntimeException(ex);
        }
        md.update(text.getBytes());
        BigInteger hash = new BigInteger(1, md.digest());
        return hash.toString(HEX);
    }
