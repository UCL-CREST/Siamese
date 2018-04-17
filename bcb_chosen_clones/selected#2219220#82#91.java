    public static String md5(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        StringBuffer result = new StringBuffer();
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(input.getBytes("utf-8"));
        byte[] digest = md.digest();
        for (byte b : digest) {
            result.append(String.format("%02X ", b & 0xff));
        }
        return result.toString();
    }
