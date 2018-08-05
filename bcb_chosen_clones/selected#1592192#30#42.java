    private StringBuffer encoder(String arg) {
        if (arg == null) {
            arg = "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update(arg.getBytes(SysConstant.charset));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toHex(md5.digest());
    }
