    private static String format(String check) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        check = check.replaceAll(" ", "");
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(check.getBytes("ISO-8859-1"));
        byte[] end = md5.digest();
        String digest = "";
        for (int i = 0; i < end.length; i++) {
            digest += ((end[i] & 0xff) < 16 ? "0" : "") + Integer.toHexString(end[i] & 0xff);
        }
        return digest;
    }
