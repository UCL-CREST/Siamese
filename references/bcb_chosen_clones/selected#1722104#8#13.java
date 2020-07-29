    public static String getMD5(String str) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.reset();
        md.update(str.getBytes());
        return bytesToString(md.digest());
    }
