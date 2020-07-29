    public static String hashMD5(String baseString) {
        MessageDigest digest = null;
        StringBuffer hexString = new StringBuffer();
        try {
            digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(baseString.getBytes());
            byte[] hash = digest.digest();
            for (int i = 0; i < hash.length; i++) {
                if ((0xff & hash[i]) < 0x10) {
                    hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
                } else {
                    hexString.append(Integer.toHexString(0xFF & hash[i]));
                }
            }
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Password.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hexString.toString();
    }
