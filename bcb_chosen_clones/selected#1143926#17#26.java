    public static String md5Encode(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(s.getBytes());
            return toHexString(md.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return s;
        }
    }
