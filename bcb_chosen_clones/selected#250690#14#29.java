    public static String getMD5(String text) {
        if (text == null) {
            return null;
        }
        String result = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance(ALG_MD5);
            md5.update(text.getBytes(ENCODING));
            result = "" + new BigInteger(1, md5.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
