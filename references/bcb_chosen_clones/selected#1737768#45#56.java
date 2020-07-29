    public static String getHash(String text) {
        String ret = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(text.getBytes());
            ret = getHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            log.error(e);
            throw new OopsException(e, "Hash Error.");
        }
        return ret;
    }
