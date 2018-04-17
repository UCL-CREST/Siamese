    public static String encodeString(String encodeType, String str) {
        if (encodeType.equals("md5of16")) {
            MD5 m = new MD5();
            return m.getMD5ofStr16(str);
        } else if (encodeType.equals("md5of32")) {
            MD5 m = new MD5();
            return m.getMD5ofStr(str);
        } else {
            try {
                MessageDigest gv = MessageDigest.getInstance(encodeType);
                gv.update(str.getBytes());
                return new BASE64Encoder().encode(gv.digest());
            } catch (java.security.NoSuchAlgorithmException e) {
                logger.error("BASE64加密失败", e);
                return null;
            }
        }
    }
