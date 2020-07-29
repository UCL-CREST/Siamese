    public static String getSHA1Hash(String plainText) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA1");
        md.update(plainText.getBytes());
        byte[] mdbytes = md.digest();
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < mdbytes.length; i++) {
            String hex = Integer.toHexString(0xFF & mdbytes[i]);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString().toUpperCase();
    }
