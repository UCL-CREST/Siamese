    public static String getEncodedHex(String text) {
        MessageDigest md = null;
        String encodedString = null;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(text.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        Hex hex = new Hex();
        encodedString = new String(hex.encode(md.digest()));
        md.reset();
        return encodedString;
    }
