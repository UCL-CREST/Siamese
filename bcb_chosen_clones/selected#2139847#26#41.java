    public static String MD5(String text) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] md5hash = new byte[32];
            md.update(text.getBytes("iso-8859-1"), 0, text.length());
            md5hash = md.digest();
            return convertToHex(md5hash);
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            return text;
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            return text;
        }
    }
