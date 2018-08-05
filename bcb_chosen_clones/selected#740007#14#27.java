    public static String encrypt(String value) {
        MessageDigest messageDigest;
        byte[] raw = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA");
            messageDigest.update(((String) value).getBytes("UTF-8"));
            raw = messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return (new BASE64Encoder()).encode(raw);
    }
