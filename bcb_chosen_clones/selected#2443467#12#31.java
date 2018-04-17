    public static String getMD5Hash(String data) {
        MessageDigest digest;
        try {
            digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(data.getBytes());
            byte[] hash = digest.digest();
            StringBuffer hexString = new StringBuffer();
            String hexChar = "";
            for (int i = 0; i < hash.length; i++) {
                hexChar = Integer.toHexString(0xFF & hash[i]);
                if (hexChar.length() < 2) {
                    hexChar = "0" + hexChar;
                }
                hexString.append(hexChar);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
    }
