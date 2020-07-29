    public String getShortToken(String md5Str) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update(md5Str.getBytes(JspRunConfig.charset));
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringBuffer token = toHex(md5.digest());
        return token.substring(8, 24);
    }
