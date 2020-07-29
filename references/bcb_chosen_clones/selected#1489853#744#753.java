    public static String md5(String value) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(value.getBytes());
            return bytesToString(messageDigest.digest());
        } catch (Exception ex) {
            Tools.logException(Tools.class, ex, value);
        }
        return value;
    }
