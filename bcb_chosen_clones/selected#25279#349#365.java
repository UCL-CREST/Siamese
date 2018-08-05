    public static String md5(String plain) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            PApplet.println("[ERROR]: md5()   " + e);
            return "";
        }
        md5.reset();
        md5.update(plain.getBytes());
        byte[] result = md5.digest();
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < result.length; i += 1) {
            hexString.append(Integer.toHexString(0xFF & result[i]));
        }
        return hexString.toString();
    }
