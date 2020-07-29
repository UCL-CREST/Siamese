    public static String getMD5Hash(String in) {
        StringBuffer result = new StringBuffer(32);
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(in.getBytes());
            Formatter f = new Formatter(result);
            for (byte b : md5.digest()) {
                f.format("%02x", b);
            }
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return result.toString();
    }
