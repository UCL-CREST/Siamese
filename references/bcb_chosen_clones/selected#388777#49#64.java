    public static String md5(String string) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException exception) {
            LOGGER.warn(exception.getMessage());
        }
        byte[] md5hash = new byte[32];
        try {
            md.update(string.getBytes("iso-8859-1"), 0, string.length());
        } catch (UnsupportedEncodingException exception) {
            LOGGER.warn(exception.getMessage());
        }
        md5hash = md.digest();
        return convertToHex(md5hash);
    }
