    public static String createHash(String seed) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Can't happen!", e);
        }
        try {
            md.update(seed.getBytes(CHARSET));
            md.update(String.valueOf(System.currentTimeMillis()).getBytes(CHARSET));
            return toHexString(md.digest());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Can't happen!", e);
        }
    }
