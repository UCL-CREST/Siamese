    public static String toHash(String pw) throws Exception {
        final MessageDigest md5 = MessageDigest.getInstance("md5");
        md5.update(pw.getBytes("utf-8"));
        final byte[] result = md5.digest();
        return toHexString(result);
    }
