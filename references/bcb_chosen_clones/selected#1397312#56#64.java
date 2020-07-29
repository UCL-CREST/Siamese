    public static String md5(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance(MD);
            md.update(data.getBytes(UTF8));
            return encodeHex(md.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
