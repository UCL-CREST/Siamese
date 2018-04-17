    public static String toMD5Sum(String arg0) {
        String ret;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(arg0.getBytes());
            ret = toHexString(md.digest());
        } catch (Exception e) {
            ret = arg0;
        }
        return ret;
    }
