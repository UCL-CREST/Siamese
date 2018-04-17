    public static String hashSourceCode(String source) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(source.getBytes());
            return new sun.misc.BASE64Encoder().encode(md.digest());
        } catch (NoSuchAlgorithmException e) {
            _log.error("Failed to generate hashcode.", e);
        }
        return null;
    }
