    public static String encrypt(String text) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] md5hash = new byte[32];
            md.update(text.getBytes("UTF-8"), 0, text.length());
            md5hash = md.digest();
            return convertToHex(md5hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
