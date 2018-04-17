    public static String md5Hash(String src) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(src.getBytes());
            return bytesArrayToHexString(md.digest());
        } catch (Exception e) {
            return null;
        }
    }
