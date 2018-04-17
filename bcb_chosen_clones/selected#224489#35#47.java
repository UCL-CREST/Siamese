    public static String encode(String arg) {
        if (arg == null) {
            arg = "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update(arg.getBytes(JavaCenterHome.JCH_CHARSET));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toHex(md5.digest());
    }
