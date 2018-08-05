    public static String hash(String text) throws Exception {
        StringBuffer hexString;
        MessageDigest mdAlgorithm = MessageDigest.getInstance("MD5");
        mdAlgorithm.update(text.getBytes());
        byte[] digest = mdAlgorithm.digest();
        hexString = new StringBuffer();
        for (int i = 0; i < digest.length; i++) {
            text = Integer.toHexString(0xFF & digest[i]);
            if (text.length() < 2) {
                text = "0" + text;
            }
            hexString.append(text);
        }
        return hexString.toString();
    }
