    public static String md5Encrypt(final String txt) {
        String enTxt = txt;
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            logger.error("Error:", e);
        }
        if (null != md) {
            byte[] md5hash = new byte[32];
            try {
                md.update(txt.getBytes("UTF-8"), 0, txt.length());
            } catch (UnsupportedEncodingException e) {
                logger.error("Error:", e);
            }
            md5hash = md.digest();
            StringBuffer md5StrBuff = new StringBuffer();
            for (int i = 0; i < md5hash.length; i++) {
                if (Integer.toHexString(0xFF & md5hash[i]).length() == 1) {
                    md5StrBuff.append("0").append(Integer.toHexString(0xFF & md5hash[i]));
                } else {
                    md5StrBuff.append(Integer.toHexString(0xFF & md5hash[i]));
                }
            }
            enTxt = md5StrBuff.toString();
        }
        return enTxt;
    }
