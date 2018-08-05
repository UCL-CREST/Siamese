    public static String sha1(String in) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] data = new byte[40];
        try {
            md.update(in.getBytes("iso-8859-1"), 0, in.length());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        data = md.digest();
        return HexidecimalUtilities.convertFromByteArrayToHex(data);
    }
