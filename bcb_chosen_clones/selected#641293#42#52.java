    public static byte[] getMD5(String source) {
        byte[] tmp = null;
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            md.update(source.getBytes());
            tmp = md.digest();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmp;
    }
