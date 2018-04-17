    public static String md5(String value) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        try {
            messageDigest.update(value.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            messageDigest.update(value.getBytes());
        }
        byte[] bytes = messageDigest.digest();
        return byteArrayToHexString(bytes);
    }
