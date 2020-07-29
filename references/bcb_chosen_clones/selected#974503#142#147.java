    private static byte[] createMD5(String seed) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.reset();
        md5.update(seed.getBytes("UTF-8"));
        return md5.digest();
    }
