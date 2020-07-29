    public static String getMD5(String value) {
        if (StringUtils.isBlank(value)) return null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(value.getBytes("UTF-8"));
            return toHexString(md.digest());
        } catch (Throwable e) {
            return null;
        }
    }
