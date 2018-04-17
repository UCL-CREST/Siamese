    public static String getMD5EncodedString(String strIn) {
        try {
            java.security.MessageDigest md5 = java.security.MessageDigest.getInstance("MD5");
            md5.reset();
            md5.update(strIn.getBytes());
            byte[] digest = md5.digest();
            StringBuffer hexString = new StringBuffer();
            for (byte aDigest : digest) {
                hexString.append(Integer.toHexString(0xFF & aDigest));
            }
            return hexString.toString();
        } catch (java.security.NoSuchAlgorithmException n) {
            return "";
        }
    }
