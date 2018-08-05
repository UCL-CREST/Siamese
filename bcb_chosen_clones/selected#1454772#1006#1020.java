    public static String getMd5Hash(String text) {
        StringBuffer result = new StringBuffer(32);
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(text.getBytes());
            Formatter f = new Formatter(result);
            byte[] digest = md5.digest();
            for (int i = 0; i < digest.length; i++) {
                f.format("%02x", new Object[] { new Byte(digest[i]) });
            }
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return result.toString();
    }
