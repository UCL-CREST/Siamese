    public static String do_checksum(String data) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        StringBuffer strbuf = new StringBuffer();
        md5.update(data.getBytes(), 0, data.length());
        byte[] digest = md5.digest();
        for (int i = 0; i < digest.length; i++) {
            strbuf.append(toHexString(digest[i]));
        }
        return strbuf.toString();
    }
