    public static String sha1(String src) {
        MessageDigest md1 = null;
        try {
            md1 = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            md1.update(src.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return hex(md1.digest());
    }
