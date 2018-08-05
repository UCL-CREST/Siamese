    public static String getHashText(String plainText, String algorithm) throws NoSuchAlgorithmException {
        MessageDigest mdAlgorithm = MessageDigest.getInstance(algorithm);
        mdAlgorithm.update(plainText.getBytes());
        byte[] digest = mdAlgorithm.digest();
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < digest.length; i++) {
            plainText = Integer.toHexString(0xFF & digest[i]);
            if (plainText.length() < 2) {
                plainText = "0" + plainText;
            }
            hexString.append(plainText);
        }
        return hexString.toString();
    }
