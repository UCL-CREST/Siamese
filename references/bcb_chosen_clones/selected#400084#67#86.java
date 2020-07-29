    private String getMD5Hash(String password) {
        String plainText = password;
        MessageDigest mdAlgorithm;
        StringBuffer hexString = new StringBuffer();
        try {
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
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return hexString.toString();
    }
