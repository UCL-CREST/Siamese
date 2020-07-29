    public static String encrypt(String text) throws NoSuchAlgorithmException {
        MessageDigest md;
        md = MessageDigest.getInstance("MD5");
        byte[] md5hash = new byte[32];
        try {
            md.update(text.getBytes("iso-8859-1"), 0, text.length());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        md5hash = md.digest();
        return convertToHex(md5hash);
    }
