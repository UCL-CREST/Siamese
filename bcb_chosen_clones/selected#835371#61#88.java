    public static String shaEncrypt(final String txt) {
        String enTxt = txt;
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            logger.error("Error:", e);
        }
        if (null != md) {
            byte[] shahash = new byte[32];
            try {
                md.update(txt.getBytes("UTF-8"), 0, txt.length());
            } catch (UnsupportedEncodingException e) {
                logger.error("Error:", e);
            }
            shahash = md.digest();
            StringBuffer md5StrBuff = new StringBuffer();
            for (int i = 0; i < shahash.length; i++) {
                if (Integer.toHexString(0xFF & shahash[i]).length() == 1) {
                    md5StrBuff.append("0").append(Integer.toHexString(0xFF & shahash[i]));
                } else {
                    md5StrBuff.append(Integer.toHexString(0xFF & shahash[i]));
                }
            }
            enTxt = md5StrBuff.toString();
        }
        return enTxt;
    }
