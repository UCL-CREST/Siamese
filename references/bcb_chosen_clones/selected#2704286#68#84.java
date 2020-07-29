    private String getMD5Password(String plainText) throws NoSuchAlgorithmException {
        MessageDigest mdAlgorithm;
        StringBuffer hexString = new StringBuffer();
        String md5Password = "";
        mdAlgorithm = MessageDigest.getInstance("MD5");
        mdAlgorithm.update(plainText.getBytes());
        byte[] digest = mdAlgorithm.digest();
        for (int i = 0; i < digest.length; i++) {
            plainText = Integer.toHexString(0xFF & digest[i]);
            if (plainText.length() < 2) {
                plainText = "0" + plainText;
            }
            hexString.append(plainText);
        }
        md5Password = hexString.toString();
        return md5Password;
    }
