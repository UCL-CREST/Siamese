    public static String MD5(String text) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(text.getBytes());
        byte[] md5hash = md.digest();
        return convertToHex(md5hash);
    }
