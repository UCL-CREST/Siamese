    public static String getMD5(String s) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.reset();
        md5.update(s.getBytes());
        byte[] result = md5.digest();
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < result.length; i++) {
            hexString.append(String.format("%02x", 0xFF & result[i]));
        }
        return hexString.toString();
    }
