    public static String MD5(String text) {
        byte[] md5hash = new byte[32];
        try {
            MessageDigest md;
            md = MessageDigest.getInstance("MD5");
            md.update(text.getBytes("iso-8859-1"), 0, text.length());
            md5hash = md.digest();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertToHex(md5hash);
    }
